package com.stephengrice.laser.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.TransactionCursorAdapter;
import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import java.text.DecimalFormat;

import static android.R.attr.fragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransactionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Cursor mCursor;
    private SQLiteDatabase mDatabase;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionsFragment newInstance(String param1, String param2) {
        TransactionsFragment fragment = new TransactionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        // Floating Action Button code
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.transactions_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AddFragment
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, new AddFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


        // Create adapter
        // Select all rows
        DbHelper dbHelper = new DbHelper(getActivity());
        mDatabase = dbHelper.getWritableDatabase();
        mCursor = mDatabase.rawQuery(DbContract.Transaction.SQL_SELECT_ALL, null);
        TransactionCursorAdapter adapter = new TransactionCursorAdapter(getActivity(), mCursor);

        // Set adapter for ListView
        ListView listView = (ListView)view.findViewById(R.id.transactions_listview);
        listView.setAdapter(adapter);
        // On Item Click: Launch TransactionDetailsFragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                DbContract.Transaction transaction = new DbContract.Transaction(
                        cursor.getFloat(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_AMOUNT)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.Transaction.COLUMN_NAME_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Category.COLUMN_NAME_TITLE))
                );
                Fragment fragment = TransactionDetailFragment.newInstance(transaction);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        float balance = DbHelper.getBalance(getActivity());
        String formattedBalance = MainActivity.formatCurrency(balance);
        // Set number of rows
        TextView txtTransactions = (TextView)view.findViewById(R.id.transactions_num_transactions);
        txtTransactions.setText(
                        Integer.toString(mCursor.getCount()) +
                        " transaction" + (mCursor.getCount() == 1 ? "" : "s") +
                        " : " + formattedBalance
        );

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
        if (mDatabase != null) {
            mDatabase.close();
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
}
