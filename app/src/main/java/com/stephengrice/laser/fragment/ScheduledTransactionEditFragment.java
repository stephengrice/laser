package com.stephengrice.laser.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.SpellCheckerInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.stephengrice.laser.CategoryArrayAdapter;
import com.stephengrice.laser.CategoryCursorAdapter;
import com.stephengrice.laser.MainActivity;
import com.stephengrice.laser.R;
import com.stephengrice.laser.RepeatType;
import com.stephengrice.laser.db.DbContract;
import com.stephengrice.laser.db.DbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduledTransactionEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduledTransactionEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduledTransactionEditFragment extends Fragment {
    private static final String ARG_ST = "scheduledTransaction";

    private OnFragmentInteractionListener mListener;

    private DbContract.ScheduledTransaction mScheduledTransaction;
    private View mView;
    private Calendar mCalendar;
    private TextView mDateView;
    private ToggleButton mToggleEarned, mToggleSpent;

    public ScheduledTransactionEditFragment() {
        // Required empty public constructor
    }

    public static ScheduledTransactionEditFragment newInstance(DbContract.ScheduledTransaction st) {
        ScheduledTransactionEditFragment fragment = new ScheduledTransactionEditFragment();
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
        mView = inflater.inflate(R.layout.fragment_scheduled_transaction_edit, container, false);

        mDateView = (TextView) mView.findViewById(R.id.txt_st_date);
        mCalendar = Calendar.getInstance();

        mToggleEarned = (ToggleButton) mView.findViewById(R.id.btn_earned);
        mToggleSpent = (ToggleButton) mView.findViewById(R.id.btn_spent);

        mToggleEarned.setChecked(true);
        mToggleSpent.setChecked(false);
        mToggleSpent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTintGray));
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



        Button btnEdit = (Button) mView.findViewById(R.id.btn_save_st);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransaction();
            }
        });

        // Create adapter for categories autocomplete
        ArrayList<DbContract.Category> categories = DbHelper.getCategories(getContext());
        CategoryArrayAdapter adapter = new CategoryArrayAdapter(getContext(), categories);
        // Set adapter
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) mView.findViewById(R.id.txt_st_category_autocomplete);
        autoComplete.setAdapter(adapter);

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
                                mScheduledTransaction.date = mCalendar.getTimeInMillis();
                                updateTimeView();
                            }
                        }, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), false);
                        timePicker.show();
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
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

    private void fillForm() {
        EditText txtTransactionAmount = (EditText) mView.findViewById(R.id.txt_st_amount);
        EditText txtTransactionDescription = (EditText) mView.findViewById(R.id.txt_st_description);
        AutoCompleteTextView txtTransactionCategory = (AutoCompleteTextView) mView.findViewById(R.id.txt_st_category_autocomplete);
        Spinner repeatSpinner = (Spinner) mView.findViewById(R.id.spinner_repeat);
        CheckBox chkEnabled = (CheckBox) mView.findViewById(R.id.chk_enabled);

        txtTransactionAmount.setText(Float.toString(Math.abs(mScheduledTransaction.amount)));
        txtTransactionDescription.setText(mScheduledTransaction.description);
        txtTransactionCategory.setText(mScheduledTransaction.category_title);
        mToggleEarned.setChecked(mScheduledTransaction.amount >= 0);
        mToggleSpent.setChecked(!mToggleEarned.isChecked());
        updateButtonColors();
        repeatSpinner.setSelection(mScheduledTransaction.repeat.getValue());
        mDateView.setText(MainActivity.formatDate(getContext(), mScheduledTransaction.date));
        chkEnabled.setChecked(mScheduledTransaction.enabled);
    }

    private boolean updateTransaction() {
        if (mView == null)
            return false;
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

        // Select elements containing user input
        EditText txtTransactionAmount = (EditText) mView.findViewById(R.id.txt_st_amount);
        EditText txtTransactionDescription = (EditText) mView.findViewById(R.id.txt_st_description);
        AutoCompleteTextView txtTransactionCategory = (AutoCompleteTextView) mView.findViewById(R.id.txt_st_category_autocomplete);
        Spinner repeatSpinner = (Spinner) mView.findViewById(R.id.spinner_repeat);
        CheckBox chkEnabled = (CheckBox) mView.findViewById(R.id.chk_enabled);

        // Populate mTransaction with new values
        boolean positive = mToggleEarned.isChecked();
        try {
            mScheduledTransaction.amount = Float.parseFloat(txtTransactionAmount.getText().toString());
        } catch(NumberFormatException e) {
            mScheduledTransaction.amount = 0;
        }
        mScheduledTransaction.description = txtTransactionDescription.getText().toString();
        if (!positive) {
            mScheduledTransaction.amount = -mScheduledTransaction.amount;
        }
        mScheduledTransaction.category_title = txtTransactionCategory.getText().toString();
        // Keep same date
        mScheduledTransaction.category_id = DbHelper.getCategoryId(getContext(), mScheduledTransaction.category_title);
        mScheduledTransaction.repeat = RepeatType.fromInt(repeatSpinner.getSelectedItemPosition());
        mScheduledTransaction.enabled = chkEnabled.isChecked();

        // Validate input - only transactionAmount is required and must be properly parsed
        if (mScheduledTransaction.amount == 0) {
            Snackbar.make(mView, getContext().getString(R.string.enter_an_amount), MainActivity.SNACKBAR_TIME).show();
            txtTransactionAmount.requestFocus();
            return false;
        }

        long rowsAffected = DbHelper.updateScheduledTransaction(getContext(), mScheduledTransaction);

        if (rowsAffected < 1) {
            Snackbar.make(mView, getContext().getString(R.string.db_error), MainActivity.SNACKBAR_TIME).show();
            return false;
        } else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new ScheduledTransactionsFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
    }

    private void updateTimeView() {
        mDateView.setText(MainActivity.formatDate(getActivity(), mScheduledTransaction.date));
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
