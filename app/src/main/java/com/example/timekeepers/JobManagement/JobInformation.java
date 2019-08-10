package com.example.timekeepers.JobManagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
     *
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

    // TODO: prevent user from editing the job if user is clocked in to that job.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_entry) {
            editJobEntry();
            return true;
        }
        if (id == R.id.delete_entry) {
            deleteJob();
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

        addressFormat();

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

    @SuppressLint("SetTextI18n")
    private void addressFormat() {
        String passedStreet1 = jobInformation.getString(getString(R.string.street1Key));
        String passedStreet2 = jobInformation.getString(getString(R.string.street2Key));
        String passedCity = jobInformation.getString(getString(R.string.cityKey));
        String passedState = jobInformation.getString(getString(R.string.stateKey));
        String passedZipCode = jobInformation.getString(getString(R.string.zipCodeKey));

        // Assertions
        assert passedStreet1 != null;
        assert passedStreet2 != null;
        assert passedCity != null;
        assert passedState != null;
        assert passedZipCode != null;

        jobAddress.setText(passedStreet1);
        if (!passedStreet2.isEmpty()) {
            if (!jobAddress.getText().toString().isEmpty()) {
                jobAddress.append("\n");
            }
            jobAddress.append(passedStreet2);
        }
        if (!passedCity.isEmpty()) {
            if (!jobAddress.getText().toString().isEmpty()) {
                jobAddress.append("\n");
            }
            jobAddress.append(passedCity);
        }
        if (!passedState.isEmpty()) {
            jobAddress.append(" " + passedState);
        }
        if (!passedZipCode.isEmpty()) {
            jobAddress.append(" " + passedZipCode);
        }

        if (!jobAddress.getText().toString().isEmpty()) {
            // Underline text
            jobAddress.setPaintFlags(jobAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            // Set up navigation to maps for directions or reference
            jobAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + jobAddress.getText().toString());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(Objects.requireNonNull(
                            getActivity()).getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });
        } else {
            jobAddress.setText("No Address Entered");
        }
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
        // TODO: Create delete job entries once implemented
//        deleteJobEntries(jobSelected);

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
