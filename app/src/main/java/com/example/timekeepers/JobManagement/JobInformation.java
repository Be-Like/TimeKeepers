package com.example.timekeepers.JobManagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timekeepers.AddressFormat;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobInformation extends Fragment {
    private MainActivity mainActivity;
    private static final String BUNDLE_KEY = "PassedBundle";
    private Bundle jobInformation;

    // Key variables needed for logic throughout
    private String jobId;
    private Boolean jobIsCompleted;
    private double jobGrossPay;
    private String jobType;

    // Text View Declarations
    private AppCompatTextView jobTitle;
    private AppCompatTextView payRateLabel;
    private AppCompatTextView payRate;
    private AppCompatTextView completedJobs;
    private AppCompatTextView jobEmail;
    private AppCompatTextView jobFederal;
    private AppCompatTextView medicare;
    private AppCompatTextView socialSecurity;
    private AppCompatTextView otherWithholdings;
    private AppCompatTextView jobPhone;
    private AppCompatTextView retirement;
    private AppCompatTextView stateTax;
    private AppCompatTextView jobWebsite;
    private AppCompatTextView jobAddress;

    public JobInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param bundle Parameter 1.
     * @return A new instance of fragment JobInformation.
     */
    public static JobInformation newInstance(Bundle bundle) {
        JobInformation fragment = new JobInformation();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobInformation = getArguments().getBundle(BUNDLE_KEY);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Toolbar navigation
        mainActivity.toolbar.setTitle("Job Info");
        mainActivity.lockNavigationDrawer(true);
        setHasOptionsMenu(true);

        // Instantiate Fragment View
        View fragmentView = inflater.inflate(R.layout.fragment_job_information,
                container, false);

        // Initialize and set views
        initializeViews(fragmentView);

        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.edit_entry, menu);
        getActivity().getMenuInflater().inflate(R.menu.delete_entry, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // TODO: prevent user from editing or deleting the job if user is clocked in to that job.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_entry) {
            editJobEntry();
            return true;
        }
        if (id == R.id.delete_entry) {
            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle("Delete Job?")
                    .setMessage("Are you sure you want to delete this job? Deleting job " +
                            "will remove all job entries and job expenses related to the job. " +
                            "This is permanent.")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteJob();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        setViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initializeViews(@NonNull View fragmentView) {
        jobTitle = fragmentView.findViewById(R.id.job_title);
        completedJobs = fragmentView.findViewById(R.id.job_status);
        payRateLabel = fragmentView.findViewById(R.id.pay_rate_label);
        payRate = fragmentView.findViewById(R.id.pay_rate);
        jobAddress = fragmentView.findViewById(R.id.address);
        jobPhone = fragmentView.findViewById(R.id.phone_number);
        jobEmail = fragmentView.findViewById(R.id.email);
        jobWebsite = fragmentView.findViewById(R.id.website);
        jobFederal = fragmentView.findViewById(R.id.federal_tax);
        stateTax = fragmentView.findViewById(R.id.state_tax);
        socialSecurity = fragmentView.findViewById(R.id.social_security);
        medicare = fragmentView.findViewById(R.id.medicare);
        retirement = fragmentView.findViewById(R.id.individual_retirement);
        otherWithholdings = fragmentView.findViewById(R.id.other_withholdings);
    }
    @SuppressLint("SetTextI18n")
    private void setViews() {
        // Needed Variables for throughout rest of logic
        jobId = jobInformation.getString(getString(R.string.idKey));
        jobIsCompleted = jobInformation.getBoolean(getString(R.string.completedJobKey));
        jobGrossPay = jobInformation.getDouble(getString(R.string.grossPayKey));
        jobType = jobInformation.getString(getString(R.string.jobTypeKey));

        NumberFormat currency = NumberFormat.getCurrencyInstance();

        jobTitle.setText(jobInformation.getString(getString(R.string.jobTitleKey)));

        if (jobIsCompleted) {
            completedJobs.setText(getString(R.string.job_completed));
        } else {
            completedJobs.setText(getString(R.string.job_incomplete));
        }

        payRate.setText(currency.format(jobInformation.getDouble(getString(R.string.payRateKey))));
        if (getString(R.string.project).equals(jobType)) {
            payRateLabel.setText("Pay");
        } else {
            payRate.append(" /hr");
        }

        AddressFormat addressFormat = new AddressFormat(
                jobInformation.getString(getString(R.string.street1Key)),
                jobInformation.getString(getString(R.string.street2Key)),
                jobInformation.getString(getString(R.string.cityKey)),
                jobInformation.getString(getString(R.string.stateKey)),
                jobInformation.getString(getString(R.string.zipCodeKey))
        );
        jobAddress.setText(addressFormat.addressFormat());
        addressFormat.createMapNavigation(jobAddress, getActivity());

        jobPhone.setText(jobInformation.getString(getString(R.string.jobPhoneKey)));
        Linkify.addLinks(jobPhone, Linkify.PHONE_NUMBERS);
        jobEmail.setText(jobInformation.getString(getString(R.string.jobEmailKey)));
        Linkify.addLinks(jobEmail, Linkify.EMAIL_ADDRESSES);
        jobWebsite.setText(jobInformation.getString(getString(R.string.jobWebsiteKey)));
        Linkify.addLinks(jobWebsite, Linkify.WEB_URLS);

        double tmpFederal = jobInformation.getDouble(getString(R.string.federalTaxKey));
        double tmpState = jobInformation.getDouble(getString(R.string.stateTaxKey));
        double tmpSocial = jobInformation.getDouble(getString(R.string.socialSecurityKey));
        double tmpMedicare = jobInformation.getDouble(getString(R.string.medicareKey));
        double tmpRetirement = jobInformation.getDouble(getString(R.string.retirementKey));
        double tmpOther = jobInformation.getDouble(getString(R.string.otherWithholdingsKey));

        jobFederal.setText(tmpFederal + " %");
        stateTax.setText(tmpState + " %");
        socialSecurity.setText(tmpSocial + " %");
        medicare.setText(tmpMedicare + " %");
        retirement.setText(tmpRetirement + " %");
        otherWithholdings.setText(tmpOther + " %");
    }

    // Entry Manipulations
    private void deleteJob() {
        Log.d(TAG, "deleteJobEntry: delete initiated");

        final DocumentReference userInfo = FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(mainActivity.getUsersEmail());

        CollectionReference jobSelected = userInfo.collection("Users_Jobs");

        // First Delete Expenses where job = passedJobTitle
        // TODO: Create delete expense entries once implemented
//        deleteExpenseEntry(jobSelected);

        // Second, Delete Job_Entries where job = passedJobTitle
        deleteJobEntries(jobSelected);

        // Third, Delete Job with jobId = passedJobId (passedJobId)
        jobSelected.document(jobId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userInfo.update("Total_Jobs", FieldValue.increment(-1));
                        userInfo.update("Gross_Pay",
                                FieldValue.increment(-1 * jobGrossPay));
                        if (!jobIsCompleted) {
                            userInfo.update("Active_Jobs", FieldValue.increment(-1));
                        }
                        Toast.makeText(getContext(), "Delete was successful",
                                Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    }
                });
    }
    private void editJobEntry() {
        AddJob editJob = AddJob.newInstance(jobType, jobInformation);
        editJob.setTargetFragment(JobInformation.this, 2015);

        FragmentManager fragmentManager =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, editJob, "Some Tag")
                .addToBackStack(editJob.getClass().getName())
                .commit();
    }
    private void deleteJobEntries(CollectionReference jobReference) {
        final CollectionReference jobEntries = jobReference.document(jobId)
                .collection("Job_Entries");
        jobEntries.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            jobEntries.document(doc.getId()).delete();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2015) {
                jobInformation = data.getBundleExtra("Job Edit Information");
                Log.d(TAG, "onActivityResult: passed job information " + jobInformation.toString());
            }
        }
    }
}
