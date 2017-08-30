package com.stephengrice.laser.fragment;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stephengrice.laser.DashDisplayView;
import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.db.DbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashFragment newInstance(String param1, String param2) {
        DashFragment fragment = new DashFragment();
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
        View view = inflater.inflate(R.layout.fragment_dash, container, false);

        // Floating Action Button code
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to TransactionAddFragment
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, new TransactionAddFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Query DB and determine total balance
        float balance = DbHelper.getBalance(getActivity());
        // Fill view
        TextView txtBalance = (TextView) view.findViewById(R.id.txt_balance);
        txtBalance.setText(MainActivity.formatCurrency(balance));
        GradientDrawable gradientDrawable = (GradientDrawable) txtBalance.getBackground();

        if (balance >= 0) {
            gradientDrawable.setColor(ContextCompat.getColor(getActivity(), R.color.colorTintGreen));
        } else {
            gradientDrawable.setColor(ContextCompat.getColor(getActivity(), R.color.colorTintRed));
        }

        DashDisplayView dashTransactions = (DashDisplayView) view.findViewById(R.id.dash_transactions);
        dashTransactions.setAmount((int)DbHelper.countTransactions(getContext()));
        dashTransactions.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, new TransactionAddFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        DashDisplayView dashScheduledTransactions = (DashDisplayView) view.findViewById(R.id.dash_scheduled_transactions);
        dashScheduledTransactions.setAmount((int)DbHelper.countScheduledTransactions(getContext()));
        dashScheduledTransactions.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, new ScheduledTransactionAddFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        txtBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_main, new BudgetFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
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
