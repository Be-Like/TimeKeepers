package com.example.timekeepers.Expenses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class AddExpenseEntry extends AddEditExpenseParent {
    private static final String JOB_OBJECT_KEY = "Job_Object_Key";
    private JobObject jobObject;

    private MainActivity mainActivity;
    private View fragmentView;

    public AddExpenseEntry() {
        // Required empty public constructor
    }

    public static AddExpenseEntry newInstance(JobObject object) {
        AddExpenseEntry fragment = new AddExpenseEntry();
        Bundle args = new Bundle();
        args.putSerializable(JOB_OBJECT_KEY, object);
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final String toolbarTitle = "Add Expense Entry";
        mainActivity.toolbar.setTitle(toolbarTitle);
        mainActivity.lockNavigationDrawer(true);

        // Instantiate Fragment View
        fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        initButtons();

        return fragmentView;
    }

    private MaterialButton saveButton;
    private MaterialButton cancelButton;
    private void initButtons() {
//        View buttonsLayout = fragmentView.findViewById(R.id.buttons_layout); TODO: this may be necesary
        saveButton = fragmentView.findViewById(R.id.save_button);
        cancelButton = fragmentView.findViewById(R.id.cancel_button);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.save_button:
                saveExpenseEntry();
                break;

            case R.id.cancel_button:
                Objects.requireNonNull(getActivity()).getWindow()
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
        }
    }

    private void saveExpenseEntry() {
        Toast.makeText(getContext(), "Test Save", Toast.LENGTH_SHORT).show();
        // TODO: continue here
        // Validate Fields
        // Save entry
        // Dismiss Keyboard
        // Navigate back to expense page
    }
}
