package com.example.timekeepers.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class AddEditJobParent extends Fragment implements View.OnClickListener {

    private View fragmentView;

    private AppCompatTextView startTimeView;
    private AppCompatTextView endTimeView;
    private AppCompatEditText breakTimeView;
    private AppCompatEditText entryNote;

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
        MaterialButton saveButton = fragmentView.findViewById(R.id.save_button);
        MaterialButton cancelButton = fragmentView.findViewById(R.id.cancel_button);

        jobTitleView.setText(jobTitle);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void onClick(View v) {

    }

}
