package com.example.timekeepers.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.timekeepers.Dashboard.DbWorkEntry;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddEditJobParent extends Fragment implements View.OnClickListener {

    private View fragmentView;

    private AppCompatTextView startTimeView;
    private AppCompatTextView endTimeView;
    private AppCompatEditText breakTimeView;
    private AppCompatEditText entryNote;

    private final Calendar startCalendar = Calendar.getInstance();
    private final Calendar endCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_add_job_entry, container, false);
        return fragmentView;
    }

    public void initViews(String jobTitle) {
        AppCompatTextView jobTitleView = fragmentView.findViewById(R.id.job_title);

        RelativeLayout startTimeLayout = fragmentView.findViewById(R.id.start_time_layout);
        setStartTimeView((AppCompatTextView) fragmentView.findViewById(R.id.start_time));

        RelativeLayout endTimeLayout = fragmentView.findViewById(R.id.end_time_layout);
        setEndTimeView((AppCompatTextView) fragmentView.findViewById(R.id.end_time));

        setBreakTimeView((AppCompatEditText) fragmentView.findViewById(R.id.break_time));

        setEntryNote((AppCompatEditText) fragmentView.findViewById(R.id.notes));

        jobTitleView.setText(jobTitle);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.start_time_layout:
                openDatePickerDialog(startTimeView, startCalendar).show();
                break;

            case R.id.end_time_layout:
                openDatePickerDialog(endTimeView, endCalendar).show();
                break;
        }
    }

    private DatePickerDialog openDatePickerDialog(final AppCompatTextView textView,
                                                  final java.util.Calendar calendar) {
        final int mYear = calendar.get(java.util.Calendar.YEAR);
        final int mMonth = calendar.get(java.util.Calendar.MONTH);
        final int mDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);

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
                                                  final java.util.Calendar calendar, final int year,
                                                  final int month, final int day) {
        int mHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
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

    public boolean isValidStartAndEndTime() {
        // Check that the start and end time is not null
        if ("".equals(startTimeView.getText().toString())
                || "".equals(endTimeView.getText().toString())) {
            Toast.makeText(getContext(),
                    "Invalid Start or End Time",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startCalendar.getTime().after(endCalendar.getTime())) {
            Toast.makeText(getContext(),
                    "Invalid Start or End Time",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isValidBreakTime(long startTime, long endTime, double breakTime) {
        Log.d(TAG, "saveEntry: " + startTime + "..." + endTime + "..." + breakTime);
        if (breakTime > ((float) (endTime - startTime) / (60 * 60 * 1000))) {
            Toast.makeText(getContext(), "Adjust the break time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // View Getters and Setters
    public void setStartTimeView(AppCompatTextView startTimeView) {
        this.startTimeView = startTimeView;
    }
    public AppCompatTextView getStartTimeView() {
        return this.startTimeView;
    }
    public void setEndTimeView(AppCompatTextView endTimeView) {
        this.endTimeView = endTimeView;
    }
    public AppCompatTextView getEndTimeView() {
        return this.endTimeView;
    }
    public void setBreakTimeView(AppCompatEditText breakTimeView) {
        this.breakTimeView = breakTimeView;
    }
    public AppCompatEditText getBreakTimeView() {
        return this.breakTimeView;
    }
    public void setEntryNote(AppCompatEditText entryNote) {
        this.entryNote = entryNote;
    }
    public AppCompatEditText getEntryNote() {
        return this.entryNote;
    }
    public Calendar getStartCalendar() {
        return this.startCalendar;
    }
    public Calendar getEndCalendar() {
        return this.endCalendar;
    }
}
