package com.example.timekeepers.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.timekeepers.Dashboard.DbWorkEntry;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class EditJobEntry extends AddEditJobParent {
    private static final String JOB_ENTRY_INFO = "JobEntryInfo";

    private Bundle jobEntryInfo;

    private View fragmentView;

    private SaveEditedEntryListener listener;

    public EditJobEntry(SaveEditedEntryListener listener) {
        this.listener = listener;
    }

    public static EditJobEntry newInstance(Bundle bundle, SaveEditedEntryListener listener) {
        EditJobEntry fragment = new EditJobEntry(listener);
        Bundle args = new Bundle();
        args.putBundle(JOB_ENTRY_INFO, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobEntryInfo = getArguments().getBundle(JOB_ENTRY_INFO);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = super.onCreateView(inflater, container, savedInstanceState);

        initViews(jobEntryInfo.getString(getString(R.string.jobTitleKey)));
        setViews();
        initButtons();

        return fragmentView;
    }

    private void setViews() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a dd MMM, yyyy", Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Date startTime = (Date) jobEntryInfo.getSerializable(getString(R.string.startTimeKey));
        Date endTime = (Date) jobEntryInfo.getSerializable(getString(R.string.endTimeKey));

        getStartTimeView().setText(dateFormat.format(startTime));
        getStartCalendar().setTime(startTime);
        getEndTimeView().setText(dateFormat.format(endTime));
        getEndCalendar().setTime(endTime);

        getBreakTimeView().setText(
                decimalFormat.format(
                        jobEntryInfo.getDouble(getString(R.string.breakTimeKey))
                )
        );
        getEntryNote().setText(jobEntryInfo.getString(getString(R.string.notesKey)));
    }

    private void initButtons() {
        MaterialButton saveButton = fragmentView.findViewById(R.id.save_button);
        MaterialButton cancelButton = fragmentView.findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.save_button) {
            saveEntry();
        }
        if (id == R.id.cancel_button) {
            Objects.requireNonNull(getActivity()).getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }

    private void saveEntry() {
        if (!isValidStartAndEndTime()) {
            return;
        }
        // Check that the break time is not greater than the hours worked
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

        // Find computational differences
        String jobId = jobEntryInfo.getString(getString(R.string.idKey));
        String jobEntryId = jobEntryInfo.getString(getString(R.string.jobEntryIdKey));
        double oldPay = jobEntryInfo.getDouble(getString(R.string.payKey));
        double oldHours = jobEntryInfo.getDouble(getString(R.string.hoursWorkedKey));
        double newHours = ((double) (endTime - startTime) / (60 * 60 * 1000)) - breakTime;
        double newPay = (oldPay / oldHours) * (newHours);

        DbWorkEntry workEntry =
                new DbWorkEntry();
        workEntry.editJobEntry(jobId, jobEntryId,
                startTime, endTime, breakTime, newHours, newPay, notes,
                newHours - oldHours, newPay - oldPay);

        listener.onSaveEditedEntry(
                timestampConversion(startTime), timestampConversion(endTime),
                breakTime, newHours, newPay, notes
                );
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    public interface SaveEditedEntryListener {
        void onSaveEditedEntry(Date startTime, Date endTime, double breakTime,
                               double hoursWorked, double pay, String notes);
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
    private Date timestampConversion(long value) {
        String tempString = dateFormat.format(value);
        try {
            return dateFormat.parse(tempString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
