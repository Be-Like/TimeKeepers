package com.example.timekeepers.Calendar;


import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timekeepers.AddressFormat;
import com.example.timekeepers.Dashboard.DbWorkEntry;
import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobEntryInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobEntryInfo extends Fragment {
    private static final String JOB_ENTRY_INFO = "JobEntryInfo";

    private Bundle jobEntryInfo;
    private MainActivity mainActivity;

    private View fragmentView;
    private AppCompatTextView breakTimeView;
    private AppCompatTextView endTimeView;
    private AppCompatTextView hoursWorkedView;
    private AppCompatTextView jobTitleView;
    private AppCompatTextView notesView;
    private AppCompatTextView payView;
    private AppCompatTextView startTimeView;
    private AppCompatTextView addressView;

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
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity.toolbar.setTitle(jobEntryInfo.getString(getString(R.string.jobTitleKey)));
        mainActivity.lockNavigationDrawer(true);
        setHasOptionsMenu(true);

        // Instantiate Fragment View
        fragmentView = inflater.inflate(R.layout.fragment_job_entry_info, container, false);

        initViews();
        setViews();

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.edit_entry, menu);
        getActivity().getMenuInflater().inflate(R.menu.delete_entry, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_entry) {
            // TODO: call editJobEntry
            editJobEntry();
            Toast.makeText(getContext(), "Will edit job entry.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.delete_entry) {
            deleteJobEntry();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editJobEntry() {
        EditJobEntry entry = EditJobEntry.newInstance(jobEntryInfo);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.main_fragment, entry)
                .addToBackStack(null)
                .commit();
    }

    private void deleteJobEntry() {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle("Delete Job Entry?")
                .setMessage("Are you sure you want to delete this job entry? " +
                        "Delete will be permanent.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String jobId = jobEntryInfo.getString(getString(R.string.idKey));
                        String entryId = jobEntryInfo.getString(getString(R.string.jobEntryIdKey));
                        double pay = jobEntryInfo.getDouble(getString(R.string.payKey));
                        double hoursWorked = jobEntryInfo.getDouble(getString(R.string.hoursWorkedKey));

                        JobObject jobObject = new JobObject(
                                null, null, 0.0, 0.0, 0.0, 0.0,
                                null, null, 0.0, 0.0, 0.0, 0.0,
                                0.0, null, 0.0, 0.0, null, null,
                                null, null, null, null, jobId
                        );

                        DbWorkEntry dbWorkEntry = new DbWorkEntry(jobObject, 0L, 0L,
                                0L, null);
                        dbWorkEntry.deleteJobEntry(entryId, pay, hoursWorked);
                        dbWorkEntry.updateJobEntryQuantity(-1);
                        Objects.requireNonNull(getActivity()).onBackPressed();

                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void initViews() {
        jobTitleView = fragmentView.findViewById(R.id.job_title);
        payView = fragmentView.findViewById(R.id.job_entry_pay);
        hoursWorkedView = fragmentView.findViewById(R.id.hours_worked);
        breakTimeView = fragmentView.findViewById(R.id.break_time);
        startTimeView = fragmentView.findViewById(R.id.start_time);
        endTimeView = fragmentView.findViewById(R.id.end_time);
        notesView = fragmentView.findViewById(R.id.notes);
        addressView = fragmentView.findViewById(R.id.address);
    }
    private void setViews() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        DecimalFormat df = new DecimalFormat("#.##");
        double tmpPay = jobEntryInfo.getDouble(getString(R.string.payKey));
        double tmpHours = jobEntryInfo.getDouble(getString(R.string.hoursWorkedKey));
        double tmpBreak = jobEntryInfo.getDouble(getString(R.string.breakTimeKey));

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("hh:mm a MMM dd, yyyy", Locale.US);

        AddressFormat addressFormat = new AddressFormat(
                jobEntryInfo.getString(getString(R.string.street1Key)),
                jobEntryInfo.getString(getString(R.string.street2Key)),
                jobEntryInfo.getString(getString(R.string.cityKey)),
                jobEntryInfo.getString(getString(R.string.stateKey)),
                jobEntryInfo.getString(getString(R.string.zipCodeKey))
        );

        jobTitleView.setText(jobEntryInfo.getString(getString(R.string.jobTitleKey)));
        payView.setText(currencyFormat.format(tmpPay));
        hoursWorkedView.setText(df.format(tmpHours));
        breakTimeView.setText(df.format(tmpBreak));
        startTimeView.setText(
                dateFormat.format(
                        (Date) jobEntryInfo.getSerializable(getString(R.string.startTimeKey))
                )
        );
        endTimeView.setText(
                dateFormat.format(
                        (Date) jobEntryInfo.getSerializable(getString(R.string.endTimeKey))
                )
        );
        notesView.setText(jobEntryInfo.getString(getString(R.string.notesKey)));
        addressView.setText(addressFormat.addressFormat());
        addressFormat.createMapNavigation(addressView, getActivity());
    }

}
