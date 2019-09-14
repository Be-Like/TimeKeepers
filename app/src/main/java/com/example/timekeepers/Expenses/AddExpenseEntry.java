package com.example.timekeepers.Expenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;

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

        return fragmentView;
    }

    public void onClick(View view) {
        super.onClick(view);
    }

}
