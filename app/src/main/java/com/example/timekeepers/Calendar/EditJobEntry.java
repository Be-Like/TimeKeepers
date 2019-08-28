package com.example.timekeepers.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.timekeepers.R;

public class EditJobEntry extends AddEditJobParent {
    private static final String JOB_ENTRY_INFO = "JobEntryInfo";

    private Bundle jobEntryInfo;

    private View fragmentView;

    private AppCompatTextView startTimeView;
    private AppCompatTextView endTimeView;
    private AppCompatEditText breakTimeView;
    private AppCompatEditText entryNote;

    public EditJobEntry() {
        //
    }

    public static EditJobEntry newInstance(Bundle bundle) {
        EditJobEntry fragment = new EditJobEntry();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = super.onCreateView(inflater, container, savedInstanceState);

        initViews(jobEntryInfo.getString(getString(R.string.jobTitleKey)));

        return fragmentView;
    }

}
