package com.stephengrice.laser.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.stephengrice.laser.AlarmReceiver;
import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.RepeatType;
import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduledTransactionAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduledTransactionAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduledTransactionAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DbContract.ScheduledTransaction mSt;

    private View mView;
    private TextView mDateView;
    private Calendar mCalendar;

    private EditText mAmountView;
    private ToggleButton mToggleView;
    private EditText mDescriptionView;
    private AutoCompleteTextView mCategoryView;
    private Spinner mRepeatView;


    public ScheduledTransactionAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduledTransactionAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduledTransactionAddFragment newInstance(String param1, String param2) {
        ScheduledTransactionAddFragment fragment = new ScheduledTransactionAddFragment();
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
        mView = inflater.inflate(R.layout.fragment_scheduled_transaction_add, container, false);

        mDateView = (TextView) mView.findViewById(R.id.txt_st_date);
        mAmountView = (EditText) mView.findViewById(R.id.txt_st_amount);
        mToggleView = (ToggleButton) mView.findViewById(R.id.btn_earned_spent);
        mDescriptionView = (EditText) mView.findViewById(R.id.txt_st_description);
        mCategoryView = (AutoCompleteTextView) mView.findViewById(R.id.txt_st_category_autocomplete);
        mRepeatView = (Spinner) mView.findViewById(R.id.spinner_repeat);

        mSt = new DbContract.ScheduledTransaction();
        mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        mSt.date = mCalendar.getTimeInMillis();
        updateTimeView();

        // Set onclick listener for spent/earned toggle to change bg color
        final ToggleButton btnSpentEarned = (ToggleButton) mView.findViewById(R.id.btn_earned_spent);
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

        Button btnDate = (Button) mView.findViewById(R.id.btn_change_st_date);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mCalendar = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute);
                                mSt.date = mCalendar.getTimeInMillis();
                                updateTimeView();
                            }
                        }, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), false);
                        timePicker.show();
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        Button btnSave = (Button) mView.findViewById(R.id.btn_save_st);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSt();
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

    private void updateTimeView() {
        mDateView.setText(MainActivity.formatDate(getActivity(), mSt.date));
    }

    private void saveSt() {
        float amount = 0;
        try {
            amount = Float.parseFloat(mAmountView.getText().toString());
        } catch(NumberFormatException e) {
            Snackbar.make(mView, getContext().getString(R.string.enter_an_amount), MainActivity.SNACKBAR_TIME).show();
            return;
        }
        mSt.amount = (mToggleView.isChecked() ? amount : -amount);
        mSt.date = mCalendar.getTimeInMillis();
        mSt.description = mDescriptionView.getText().toString();
        mSt.category_title = mCategoryView.getText().toString();
        mSt.category_id = DbHelper.getCategoryId(getContext(), mSt.category_title);
        mSt.repeat = getRepeatValue(mRepeatView.getSelectedItem().toString());
        mSt.enabled = true;

        mSt.id = DbHelper.insertScheduledTransaction(getContext(), mSt);

        if (mSt.id == -1) {
            Snackbar.make(mView, getContext().getString(R.string.db_error), MainActivity.SNACKBAR_TIME).show();
        } else {
            // Schedule transaction
            AlarmReceiver.setAlarm(getContext(), mSt);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new ScheduledTransactionsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private RepeatType getRepeatValue(String repeatText) {
        String[] choices = getContext().getResources().getStringArray(R.array.spinner_repeat);
        if (repeatText.equals(choices[0])) { // NO REPEAT
            return RepeatType.NO_REPEAT;
        } else if (repeatText.equals(choices[1])) { // DAILY
            return RepeatType.DAILY;
        } else if (repeatText.equals(choices[2])) {
            return RepeatType.WEEKLY;
        } else if (repeatText.equals(choices[3])) {
            return RepeatType.BI_WEEKLY;
        } else if (repeatText.equals(choices[4])) {
            return RepeatType.MONTHLY;
        } else {
            return RepeatType.YEARLY;
        }
    }
}
