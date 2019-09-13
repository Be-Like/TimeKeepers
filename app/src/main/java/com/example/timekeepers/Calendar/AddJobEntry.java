package com.example.timekeepers.Calendar;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.timekeepers.Dashboard.DbWorkEntry;
import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String toolbarTitle = "Add Job Entry";
        mainActivity.toolbar.setTitle(toolbarTitle);
        mainActivity.lockNavigationDrawer(true);

        // Instantiate Fragment View
        fragmentView = super.onCreateView(inflater, container, savedInstanceState);

        initViews(jobObject.getJobTitle());
        initViews();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void initViews() {
        MaterialButton saveButton = fragmentView.findViewById(R.id.save_button);
        MaterialButton cancelButton = fragmentView.findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.save_button:
                saveEntry();
                break;

            case R.id.cancel_button:
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getBreakTimeView().getWindowToken(), 0);
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
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
        imm.hideSoftInputFromWindow(getBreakTimeView().getWindowToken(), 0);
        Objects.requireNonNull(getActivity()).onBackPressed();
    }
}
