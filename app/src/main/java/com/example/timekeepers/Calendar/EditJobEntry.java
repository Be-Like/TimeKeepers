package com.example.timekeepers.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class EditJobEntry extends AddEditJobParent {
    private static final String JOB_ENTRY_INFO = "JobEntryInfo";

    private Bundle jobEntryInfo;

    private View fragmentView;

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
        initButtons();

        return fragmentView;
    }

    private void initButtons() {
        MaterialButton saveButton = fragmentView.findViewById(R.id.save_button);
        MaterialButton cancelButton = fragmentView.findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    // TODO: continue working on edit job entry and it's parent class.

    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.save_button) {
            Toast.makeText(getContext(), "Will save edit", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.cancel_button) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }

}
