package com.stephengrice.laser.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.db.DbHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MODE = "mode";

    private OnFragmentInteractionListener mListener;

    private View mView;
    private DbHelper.CountMode mMode;
    private PieChart mChart;

    public BudgetChartFragment() {

    }

    public static BudgetChartFragment newInstance(DbHelper.CountMode mode) {
        BudgetChartFragment fragment = new BudgetChartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMode = (DbHelper.CountMode) getArguments().getSerializable(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_budget_chart, container, false);


        return mView;
    }

    public void onResume() {
        super.onResume();

        if (mView != null) {
            mChart = (PieChart) mView.findViewById(R.id.fragment_chart);
            if (!fillChart(mChart, mMode)) {
                // No data for chart
                //Snackbar.make(mView, "No data found for this chart. Add a transaction!", MainActivity.SNACKBAR_TIME).show();
            }
        }
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

    private boolean fillChart(PieChart chart, DbHelper.CountMode mode) {
        // Get category counts
        HashMap<String, Float> categories = DbHelper.countByCategory(getActivity(), mode);

        if (categories.size() < 1) {
            return false;
        }

        // Create entries and fill
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // Populate pie entries with category data
        for (HashMap.Entry<String, Float> categoryEntry : categories.entrySet()) {
            entries.add(new PieEntry(Math.abs(categoryEntry.getValue()), categoryEntry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData();
        data.setDataSet(dataSet);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        int[] resource;
        if (mode == DbHelper.CountMode.POSITIVE) {
            resource = getContext().getResources().getIntArray(R.array.chartGreen);
        } else {
            resource = getContext().getResources().getIntArray(R.array.chartRed);
        }

        for (int c : resource) {
            colors.add(c);
        }


        dataSet.setColors(colors);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return MainActivity.formatPercent(value);
            }
        });
        dataSet.setValueTextSize(15f);

        chart.setData(data);
        chart.setEntryLabelColor(Color.BLACK);
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setEntryLabelTextSize(20f);
//        chart.setHoleRadius(0f);
        chart.setDrawHoleEnabled(false);
        chart.invalidate();

        return true;
    }
}
