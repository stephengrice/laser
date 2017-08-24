package com.stephengrice.laser.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.stephengrice.laser.CategoryCursorAdapter;
import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import java.util.Date;

public class TransactionAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Cursor mCursor;

    public TransactionAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionAddFragment newInstance(String param1, String param2) {
        TransactionAddFragment fragment = new TransactionAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_transaction_add, container, false);
        Button btnAdd = (Button) view.findViewById(R.id.btn_save_transaction);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction(view);
            }
        });

        // Create adapter for categories autocomplete
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mCursor = db.rawQuery(DbContract.Category.SQL_SELECT_ALL, null);
        CategoryCursorAdapter adapter = new CategoryCursorAdapter(getActivity(), mCursor);
        // Set adapter
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.transaction_category_autocomplete);
        autoComplete.setAdapter(adapter);

        // Set onclick listener for spent/earned toggle to change bg color
        final ToggleButton btnSpentEarned = (ToggleButton) view.findViewById(R.id.btn_earned_spent);
        btnSpentEarned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSpentEarned.isChecked()) {
                    btnSpentEarned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGreen));
                } else {
                    btnSpentEarned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintRed));
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mCursor != null) {
            mCursor.close();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addTransaction(View view) {
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Select elements containing user input
        EditText txtTransactionAmount = (EditText) view.findViewById(R.id.txt_transaction_amount);
        EditText txtTransactionDescription = (EditText) view.findViewById(R.id.txt_transaction_description);
        AutoCompleteTextView txtTransactionCategory = (AutoCompleteTextView) view.findViewById(R.id.transaction_category_autocomplete);
        ToggleButton btnEarned = (ToggleButton) view.findViewById(R.id.btn_earned_spent);
        // Get values for input to DB
        boolean positive = btnEarned.isChecked();
        float transactionAmount;
        try {
            transactionAmount = Float.parseFloat(txtTransactionAmount.getText().toString());
        } catch(NumberFormatException e) {
            transactionAmount = 0;
        }
        String transactionDescription = txtTransactionDescription.getText().toString();
        if (!positive) {
            transactionAmount = -transactionAmount;
        }

        // Validate input - only transactionAmount is required and must be properly parsed
        if (transactionAmount == 0) {
            Snackbar.make(view, "Please enter a transaction amount.", MainActivity.SNACKBAR_TIME).show();
            txtTransactionAmount.requestFocus();
            return;
        }



        // Add transaction row in database
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create ContentValues
        ContentValues values = new ContentValues();
        values.put(DbContract.Transaction.COLUMN_NAME_AMOUNT, transactionAmount);
        values.put(DbContract.Transaction.COLUMN_NAME_DATE, new Date().getTime());
        // Sort out whether to include a category_id (if the field is empty, don't)
        String mTitle = txtTransactionCategory.getText().toString();
        if (mTitle.length() >= 1) {
            // Add the category or lookup. Either way, get the row id
            long category_id = DbHelper.getCategoryId(getActivity(), txtTransactionCategory.getText().toString());
            values.put(DbContract.Transaction.COLUMN_NAME_CATEGORY_ID, category_id);
        } else {
            values.putNull(DbContract.Transaction.COLUMN_NAME_CATEGORY_ID);
        }
        values.put(DbContract.Transaction.COLUMN_NAME_DESCRIPTION, transactionDescription);
        long newRowId = db.insert(DbContract.Transaction.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Snackbar.make(view, "An error occurred.", MainActivity.SNACKBAR_TIME).show();
        } else {
            // Switch to dash fragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new TransactionsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

}
