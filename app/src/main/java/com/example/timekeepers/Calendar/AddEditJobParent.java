package com.example.timekeepers.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_add_job_entry, container, false);
        return fragmentView;
    }

    public void initViews(String jobTitle) {
        AppCompatTextView jobTitleView = fragmentView.findViewById(R.id.job_title);
        RelativeLayout startTimeLayout = fragmentView.findViewById(R.id.start_time_layout);
        startTimeView = fragmentView.findViewById(R.id.start_time);
        RelativeLayout endTimeLayout = fragmentView.findViewById(R.id.end_time_layout);
        endTimeView = fragmentView.findViewById(R.id.end_time);
        breakTimeView = fragmentView.findViewById(R.id.break_time);
        entryNote = fragmentView.findViewById(R.id.notes);

        jobTitleView.setText(jobTitle);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_layout:
                openDatePickerDialog(startTimeView, startCalendar).show();
                break;

            case R.id.end_time_layout:
                openDatePickerDialog(endTimeView, endCalendar).show();
                break;

//            case R.id.save_button:
////                saveEntry();
//                break;
//
//            case R.id.cancel_button:
//                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(breakTimeView.getWindowToken(), 0);
//                Objects.requireNonNull(getActivity()).onBackPressed();
//                break;
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

}
