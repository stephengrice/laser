package com.stephengrice.laser.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.stephengrice.laser.CategoryCursorAdapter;
import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionEditFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TRANSACTION = "transaction";

    private DbContract.Transaction mTransaction;

    private OnFragmentInteractionListener mListener;
    private View mView;
    private Cursor mCursor;
    private ToggleButton mToggleEarned, mToggleSpent;

    public TransactionEditFragment() {
        // Required empty public constructor
    }

    public static TransactionEditFragment newInstance(DbContract.Transaction param1) {
        TransactionEditFragment fragment = new TransactionEditFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRANSACTION, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTransaction = (DbContract.Transaction) getArguments().getSerializable(ARG_TRANSACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_transaction_edit, container, false);

        mToggleEarned = (ToggleButton) mView.findViewById(R.id.btn_earned);
        mToggleSpent = (ToggleButton) mView.findViewById(R.id.btn_spent);
        mToggleEarned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleEarned.setChecked(true);
                mToggleEarned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGreen));
                mToggleSpent.setChecked(false);
                mToggleSpent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGray));
            }
        });
        mToggleSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleEarned.setChecked(false);
                mToggleEarned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGray));
                mToggleSpent.setChecked(true);
                mToggleSpent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintRed));
            }
        });

        fillForm();

        Button btnEdit = (Button) mView.findViewById(R.id.btn_update_transaction);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransaction();
            }
        });

        // Create adapter for categories autocomplete
        DbHelper dbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mCursor = db.rawQuery(DbContract.Category.SQL_SELECT_ALL, null);
        CategoryCursorAdapter adapter = new CategoryCursorAdapter(getActivity(), mCursor);
        // Set adapter
        AutoCompleteTextView txtCategoryView = (AutoCompleteTextView) mView.findViewById(R.id.txt_transaction_category_autocomplete);
        txtCategoryView.setAdapter(adapter);
        txtCategoryView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                return false;
            }
        });


        return mView;
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

    private void fillForm() {
        EditText txtTransactionAmount = (EditText) mView.findViewById(R.id.txt_transaction_amount);
        EditText txtTransactionDescription = (EditText) mView.findViewById(R.id.txt_transaction_description);
        AutoCompleteTextView txtTransactionCategory = (AutoCompleteTextView) mView.findViewById(R.id.txt_transaction_category_autocomplete);

        mToggleEarned.setChecked(mTransaction.amount >= 0);
        mToggleSpent.setChecked(!mToggleEarned.isChecked());
        updateButtonColors();

        txtTransactionAmount.setText(Float.toString(Math.abs(mTransaction.amount)));
        txtTransactionDescription.setText(mTransaction.description);
        txtTransactionCategory.setText(mTransaction.category_title);
    }

    private boolean updateTransaction() {
        if (mView == null)
            return false;
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

        // Select elements containing user input
        EditText txtTransactionAmount = (EditText) mView.findViewById(R.id.txt_transaction_amount);
        EditText txtTransactionDescription = (EditText) mView.findViewById(R.id.txt_transaction_description);
        AutoCompleteTextView txtTransactionCategory = (AutoCompleteTextView) mView.findViewById(R.id.txt_transaction_category_autocomplete);

        // Populate mTransaction with new values
        boolean positive = mToggleEarned.isChecked();
        try {
            mTransaction.amount = Float.parseFloat(txtTransactionAmount.getText().toString());
        } catch(NumberFormatException e) {
            mTransaction.amount = 0;
        }
        mTransaction.description = txtTransactionDescription.getText().toString();
        if (!positive) {
            mTransaction.amount = -mTransaction.amount;
        }
        mTransaction.category_title = txtTransactionCategory.getText().toString();
        // Keep same date
        mTransaction.category_id = DbHelper.getCategoryId(getContext(), mTransaction.category_title);

        // Validate input - only transactionAmount is required and must be properly parsed
        if (mTransaction.amount == 0) {
            Snackbar.make(mView, getContext().getString(R.string.enter_an_amount), MainActivity.SNACKBAR_TIME).show();
            txtTransactionAmount.requestFocus();
            return false;
        }

        int rowsAffected = DbHelper.updateTransaction(getContext(), mTransaction);

        if (rowsAffected < 1) {
            Snackbar.make(mView, "An error occurred.", MainActivity.SNACKBAR_TIME).show();
            return false;
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new TransactionsFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
    }

    private void updateButtonColors() {
        if (mToggleEarned.isChecked()) {
            mToggleEarned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGreen));
            mToggleSpent.setChecked(false);
            mToggleSpent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGray));
        } else {
            mToggleEarned.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGray));
            mToggleSpent.setChecked(true);
            mToggleSpent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintRed));
        }
    }
}
