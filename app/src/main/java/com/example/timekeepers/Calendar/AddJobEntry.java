package com.example.timekeepers.Calendar;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timekeepers.Dashboard.DbWorkEntry;
import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddJobEntry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddJobEntry extends AddEditJobParent implements View.OnClickListener {
    private static final String JOB_OBJECT_KEY = "Job_Object_Key";
    private JobObject jobObject;

    private MainActivity mainActivity;
    private View fragmentView;

    private AppCompatTextView startTimeView;
    private AppCompatTextView endTimeView;
    private AppCompatEditText breakTimeView;
    private AppCompatEditText entryNote;

    private final Calendar startCalendar = Calendar.getInstance();
    private final Calendar endCalendar = Calendar.getInstance();

    public AddJobEntry() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param object Job Object
     * @return A new instance of fragment AddJobEntry.
     */
    public static AddJobEntry newInstance(JobObject object) {
        AddJobEntry fragment = new AddJobEntry();
        Bundle args = new Bundle();
        args.putSerializable(JOB_OBJECT_KEY, object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobObject = (JobObject) getArguments().getSerializable(JOB_OBJECT_KEY);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String toolbarTitle = "Add Job Entry";
        mainActivity.toolbar.setTitle(toolbarTitle);
        mainActivity.lockNavigationDrawer(true);

        // Instantiate Fragment View
        fragmentView = super.onCreateView(inflater, container, savedInstanceState);
//        fragmentView = inflater.inflate(R.layout.fragment_add_job_entry, container, false);

//        initViews();
        initViews(jobObject.getJobTitle());
        initViews();
        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void initViews() {
//        AppCompatTextView jobTitleView = fragmentView.findViewById(R.id.job_title);
//        RelativeLayout startTimeLayout = fragmentView.findViewById(R.id.start_time_layout);
//        startTimeView = fragmentView.findViewById(R.id.start_time);
//        RelativeLayout endTimeLayout = fragmentView.findViewById(R.id.end_time_layout);
//        endTimeView = fragmentView.findViewById(R.id.end_time);
//        breakTimeView = fragmentView.findViewById(R.id.break_time);
//        entryNote = fragmentView.findViewById(R.id.notes);
        MaterialButton saveButton = fragmentView.findViewById(R.id.save_button);
        MaterialButton cancelButton = fragmentView.findViewById(R.id.cancel_button);

//        jobTitleView.setText(jobObject.getJobTitle());
//        startTimeLayout.setOnClickListener(this);
//        endTimeLayout.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
//            case R.id.start_time_layout:
//                openDatePickerDialog(startTimeView, startCalendar).show();
//                break;
//
//            case R.id.end_time_layout:
//                openDatePickerDialog(endTimeView, endCalendar).show();
//                break;

            case R.id.save_button:
                saveEntry();
                break;

            case R.id.cancel_button:
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                // TODO: validate that this doesn't crash the application
                imm.hideSoftInputFromWindow(getBreakTimeView().getWindowToken(), 0);
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
        }
    }

    private DatePickerDialog openDatePickerDialog(final AppCompatTextView textView,
                                                  final Calendar calendar) {
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(Objects.requireNonNull(getContext()),
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                openDatePickerDialog(textView, calendar).dismiss();
                openTimePickerDialog(textView, calendar, year, month, day).show();
            }
        }, mYear, mMonth, mDay);
    }
    private TimePickerDialog openTimePickerDialog(final AppCompatTextView textView,
                                                  final Calendar calendar, final int year,
                                                  final int month, final int day) {
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                SimpleDateFormat df = new SimpleDateFormat("hh:mm a dd MMM, yyyy", Locale.US);
                calendar.set(year, month, day, hour, minute);
                textView.setText(df.format(calendar.getTime()));
                validateDateAndTimeSelected();
            }
        }, mHour, mMinute, false);
    }
    private void validateDateAndTimeSelected() {
        if (startCalendar.getTime().after(endCalendar.getTime())) {
            endTimeView.setTextColor(Color.RED);
        } else {
            endTimeView.setTextColor(Color.BLACK);
        }
    }

    private void saveEntry() {
        if (!isValidStartAndEndTime()) {
            return;
        }

        long startTime = getStartCalendar().getTimeInMillis();
        long endTime = getEndCalendar().getTimeInMillis();
        double breakTime = 0;
        if (!Objects.requireNonNull(getBreakTimeView().getText()).toString().equals("")
                && !getBreakTimeView().getText().toString().equals(".")
                && !getBreakTimeView().getText().toString().equals(",")) {
            breakTime = Double.valueOf(getBreakTimeView().getText().toString());
        }

        if (!isValidBreakTime(startTime, endTime, breakTime)) {
            return;
        }

        String notes = Objects.requireNonNull(getEntryNote().getText()).toString();
        DbWorkEntry workEntry = new DbWorkEntry(jobObject, startTime, endTime, breakTime, notes);
        workEntry.saveWorkEntryToDb();
        workEntry.updateJobEntryQuantity(1);

        // Return to Calendar
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // TODO: validate that this does not crash the application
        imm.hideSoftInputFromWindow(getBreakTimeView().getWindowToken(), 0);
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

//    private void saveEntry() {
//        // Check that the start and end time is not null
//        if ("".equals(startTimeView.getText().toString())
//                || "".equals(endTimeView.getText().toString())) {
//            Toast.makeText(getContext(),
//                    "Invalid Start or End Time",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (startCalendar.getTime().after(endCalendar.getTime())) {
//            Toast.makeText(getContext(),
//                    "Invalid Start or End Time",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Check that the break time is not greater than the hours worked
//        long startTime = startCalendar.getTimeInMillis();
//        long endTime = endCalendar.getTimeInMillis();
//        double breakTime = 0;
//        if (!Objects.requireNonNull(breakTimeView.getText()).toString().equals("")
//                && !breakTimeView.getText().toString().equals(".")
//                && !breakTimeView.getText().toString().equals(",")) {
//            breakTime = Double.valueOf(breakTimeView.getText().toString());
//        }
//
//        Log.d(TAG, "saveEntry: " + startTime + "..." + endTime + "..." + breakTime);
//        if (breakTime > ((float) (endTime - startTime) / (60 * 60 * 1000))) {
//            Toast.makeText(getContext(), "Adjust the break time", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String notes = Objects.requireNonNull(entryNote.getText()).toString();
//
//        // Save the entry using the dbWorkEntry class
//        DbWorkEntry workEntry = new DbWorkEntry(jobObject, startTime, endTime, breakTime, notes);
//        workEntry.saveWorkEntryToDb();
//        workEntry.updateJobEntryQuantity(1);
//
//        // Return to Calendar
//
//        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(breakTimeView.getWindowToken(), 0);
//        Objects.requireNonNull(getActivity()).onBackPressed();
//    }
}
