package com.stephengrice.laser.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.db.DbContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduledTransactionDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduledTransactionDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduledTransactionDetailFragment extends Fragment {
    private static final String ARG_ST = "scheduledTransaction";

    private DbContract.ScheduledTransaction mScheduledTransaction;
    private OnFragmentInteractionListener mListener;
    private View mView;

    public ScheduledTransactionDetailFragment() {
        // Required empty public constructor
    }

    public static ScheduledTransactionDetailFragment newInstance(DbContract.ScheduledTransaction st) {
        ScheduledTransactionDetailFragment fragment = new ScheduledTransactionDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ST, st);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScheduledTransaction = (DbContract.ScheduledTransaction) getArguments().getSerializable(ARG_ST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_scheduled_transaction_detail, container, false);

        TextView txtAmount = (TextView) mView.findViewById(R.id.txt_amount);
        TextView txtDate = (TextView) mView.findViewById(R.id.txt_date);
        TextView txtDescription = (TextView) mView.findViewById(R.id.txt_description);
        TextView txtCategoryTitle = (TextView) mView.findViewById(R.id.txt_category_title);

        txtAmount.setText("Amount: " + MainActivity.formatCurrency(mScheduledTransaction.amount));
        txtDate.setText(MainActivity.formatDate(getActivity(), mScheduledTransaction.date));
        txtDescription.setText("Description: " + mScheduledTransaction.description);
        txtCategoryTitle.setText("Category: " + mScheduledTransaction.category_title == null ? mView.getResources().getString(R.string.no_category) : mScheduledTransaction.category_title);

        // Floating Action Button code
        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.transaction_edit_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to TransactionAddFragment
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, ScheduledTransactionEditFragment.newInstance(mScheduledTransaction))
                        .addToBackStack(null)
                        .commit();
            }
        });

        return mView;    }

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
