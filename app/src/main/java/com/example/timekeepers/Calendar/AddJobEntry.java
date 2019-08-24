package com.example.timekeepers.Calendar;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.timekeepers.DialogPagerAdapter;
import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddJobEntry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddJobEntry extends Fragment implements View.OnClickListener {
    private static final String JOB_OBJECT_KEY = "Job_Object_Key";
    private JobObject jobObject;

    private MainActivity mainActivity;
    private View fragmentView;
    private AppCompatTextView jobTitleView;
    private RelativeLayout startTimeLayout;
    private AppCompatTextView startTimeView;
    private RelativeLayout endTimeLayout;
    private AppCompatTextView endTimeView;
    private AppCompatEditText breakTimeView;
    private AppCompatEditText entryNote;
    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    public AddJobEntry() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String toolbarTitle = "Add Job Entry";
        mainActivity.toolbar.setTitle(toolbarTitle);
        mainActivity.lockNavigationDrawer(true);

        // Instantiate Fragment View
        fragmentView = inflater.inflate(R.layout.fragment_add_job_entry, container, false);

        initViews();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void initViews() {
        jobTitleView = fragmentView.findViewById(R.id.job_title);
        startTimeLayout = fragmentView.findViewById(R.id.start_time_layout);
        startTimeView = fragmentView.findViewById(R.id.start_time);
        endTimeLayout = fragmentView.findViewById(R.id.end_time_layout);
        endTimeView = fragmentView.findViewById(R.id.end_time);
        breakTimeView = fragmentView.findViewById(R.id.break_time);
        entryNote = fragmentView.findViewById(R.id.notes);
        saveButton = fragmentView.findViewById(R.id.save_button);
        cancelButton = fragmentView.findViewById(R.id.cancel_button);

        jobTitleView.setText(jobObject.getJobTitle());
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time_layout:
                DateTimeAdapter dateTimeAdapter = new DateTimeAdapter(getContext());
                ViewPager viewPager = fragmentView.findViewById(R.id.pager);
                viewPager.setAdapter(dateTimeAdapter);
                break;

            case R.id.end_time_layout:
                break;

            case R.id.save_button:
                break;

            case R.id.cancel_button:
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity())
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(breakTimeView.getWindowToken(), 0);
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
        }
    }

    private class DateTimeAdapter extends DialogPagerAdapter {
        DatePickerDialog datePicker;
        TimePickerDialog timePicker;

        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        DateTimeAdapter(Context context) {
            super(context);
            datePicker = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            cal.set(year, month, dayOfMonth);
//                            SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
//                            dateTimePickerString = df.format(cal.getTime());
//                            startTimeView.setText(df.format(cal.getTime()));
                            Log.d("Testing Date Picker - ", "onDateSet: " + cal.getTime());
                        }
                    }, mYear, mMonth, mDay);

            timePicker = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    cal.set(mYear, mMonth, mDay, hourOfDay, minute);
                    Log.d("Testing Time Picker - ", "onDateSet: " + cal.getTime());
                }
            }, mHour, mMinute, true);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Dialog getItem(int pos) {
            switch (pos) {
                case 0:
                    return datePicker;
                case 1:
                default:
                    return timePicker;
            }
        }
    }
}
