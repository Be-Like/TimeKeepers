package com.example.timekeepers.Calendar;


import android.os.Bundle;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String JOB_ENTRY_INFO = "JobEntryInfo";

    // TODO: Rename and change types of parameters
    private Bundle jobEntryInfo;


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
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_entry_info, container, false);
    }

}
