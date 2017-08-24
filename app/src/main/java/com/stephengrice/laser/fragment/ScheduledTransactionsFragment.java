package com.stephengrice.laser.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stephengrice.laser.R;
import com.stephengrice.laser.ScheduledTransactionsArrayAdapter;
import com.stephengrice.laser.TransactionsArrayAdapter;
import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduledTransactionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduledTransactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduledTransactionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;

    private OnFragmentInteractionListener mListener;

    public ScheduledTransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduledTransactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduledTransactionsFragment newInstance(String param1, String param2) {
        ScheduledTransactionsFragment fragment = new ScheduledTransactionsFragment();
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
        mView = inflater.inflate(R.layout.fragment_scheduled_transactions, container, false);
        // Floating Action Button code
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.st_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to TransactionAddFragment
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, new ScheduledTransactionAddFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        ArrayList<DbContract.ScheduledTransaction> scs = DbHelper.getScheduledTransactions(getActivity());

        ScheduledTransactionsArrayAdapter adapter = new ScheduledTransactionsArrayAdapter(getActivity(), scs);
        // Set adapter for ListView
        ListView listView = (ListView)mView.findViewById(R.id.st_listview);
        listView.setAdapter(adapter);
        // On Item Click: Launch TransactionDetailsFragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DbContract.ScheduledTransaction sc = (DbContract.ScheduledTransaction) parent.getItemAtPosition(position);
                Fragment fragment = ScheduledTransactionDetailFragment.newInstance(sc);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment)
                        .addToBackStack(null)
                        .commit();
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
}
