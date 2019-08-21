package com.example.timekeepers.Calendar;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeepers.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobEntryInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobEntryInfo extends Fragment {
    private static final String JOB_ENTRY_INFO = "JobEntryInfo";

    private Bundle jobEntryInfo;

    private View fragmentView;
    private AppCompatTextView breakTimeView;
    private AppCompatTextView endTimeView;
    private AppCompatTextView hoursWorkedView;
    private AppCompatTextView jobTitleView;
    private AppCompatTextView notesView;
    private AppCompatTextView payView;
    private AppCompatTextView startTimeView;
    private AppCompatTextView street1View;
    private AppCompatTextView street2View;
    private AppCompatTextView cityView;
    private AppCompatTextView stateView;
    private AppCompatTextView zipcodeView;

    public JobEntryInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bundle Parameter 1.
     * @return A new instance of fragment JobEntryInfo.
     */
    public static JobEntryInfo newInstance(Bundle bundle) {
        JobEntryInfo fragment = new JobEntryInfo();
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
        fragmentView = inflater.inflate(R.layout.fragment_job_entry_info, container, false);

        initViews();

        return fragmentView;
    }

    private void initViews() {
        jobTitleView = fragmentView.findViewById(R.id.job_title);
        payView = fragmentView.findViewById(R.id.job_entry_pay);
        hoursWorkedView = fragmentView.findViewById(R.id.hours_worked);
        breakTimeView = fragmentView.findViewById(R.id.break_time);
        startTimeView = fragmentView.findViewById(R.id.start_time);
        endTimeView = fragmentView.findViewById(R.id.end_time);
        notesView = fragmentView.findViewById(R.id.notes);
        // TODO: work on creating a addressFormat class
    }

}
