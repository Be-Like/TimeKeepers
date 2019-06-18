package com.example.timekeepers.JobManagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timekeepers.CurrencyTextListener;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddJob.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddJob#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddJob extends Fragment
        implements View.OnClickListener, CalculateSalaryDialog.JobCompensationListener {

    // [START] Class Declarations
    private static final String ARG_PARAM1 = "param1";
    private static final String BUNDLE_KEY = "PassedBundle";
    private Bundle jobInformation;

    private MainActivity mainActivity;

    // View Declarations
    private View fragmentView;

    private String jobType;

    // Job Details Declarations
    private TextInputEditText jobTitle;
    private CheckBox completedCheckbox;
    private TextInputEditText payRate;

    // Address Declarations
    private AutoCompleteTextView addressLine1;
    private AutoCompleteTextView addressLine2;
    private AutoCompleteTextView city;
    private AutoCompleteTextView state;
    private AutoCompleteTextView zipcode;

    // Job Contact Info Declarations
    private AutoCompleteTextView phoneNumber;
    private AutoCompleteTextView jobEmail;
    private AutoCompleteTextView website;

    // Taxes Declarations
    private TextInputEditText federalIncome;
    private TextInputEditText stateIncome;
    private TextInputEditText socialSecurity;
    private TextInputEditText medicareInput;
    private TextInputEditText individualRetirement;
    private TextInputEditText otherWithholdings;

    // Button Declarations
    private MaterialButton saveButton;
    private MaterialButton cancelButton;
    private MaterialButton calculateRate;

    private OnFragmentInteractionListener mListener;
    // [END] Class Declarations

    public AddJob() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jobType Parameter 1.
     * @return A new instance of fragment AddJob.
     */
    public static AddJob newInstance(String jobType, Bundle bundle) {
        AddJob fragment = new AddJob();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jobType);
        args.putBundle(BUNDLE_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobType = getArguments().getString(ARG_PARAM1);
            jobInformation = getArguments().getBundle(BUNDLE_KEY);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize fragment view
        fragmentView = inflater.inflate(R.layout.fragment_add_job, container, false);

        // Set Toolbar Title and Lock Navigation Drawer
        String addJobTitle = "Add Job";
        mainActivity.toolbar.setTitle(addJobTitle + " (" + jobType + ")");
        mainActivity.lockNavigationDrawer(true);

        // Initialize form declarations
        initJobDetailDeclarations();
        initAddressDeclarations();
        initJobContactDeclarations();
        initTaxDeclarations();
        initButtonDeclarations();

        // Set views if bundle is not null
        if (jobInformation != null) {
            setViews();
        }

        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initJobDetailDeclarations() {
        jobTitle = fragmentView.findViewById(R.id.job_title);
        completedCheckbox = fragmentView.findViewById(R.id.completed_checkbox);
        payRate = fragmentView.findViewById(R.id.pay_rate);
        CurrencyTextListener currencyTextListener = new CurrencyTextListener(payRate);
        payRate.addTextChangedListener(currencyTextListener);

        if (jobType.equals(getString(R.string.salary))) {
            setTextInputLayoutHint("Hourly Rate of Annual Salary");
            calculateRate = fragmentView.findViewById(R.id.calculate_hourly_rate);
            calculateRate.setVisibility(View.VISIBLE);
            calculateRate.setOnClickListener(this);
        } else if (jobType.equals(getString(R.string.project))) {
            setTextInputLayoutHint("Pay Upon Completion");
        }
    }
    private void initAddressDeclarations() {
        addressLine1 = fragmentView.findViewById(R.id.address_first_line);
        addressLine2 = fragmentView.findViewById(R.id.address_second_line);
        city = fragmentView.findViewById(R.id.city_name);
        state = fragmentView.findViewById(R.id.state_name);
        zipcode = fragmentView.findViewById(R.id.zipcode);
    }
    private void initJobContactDeclarations() {
        phoneNumber = fragmentView.findViewById(R.id.phone_number);
        jobEmail = fragmentView.findViewById(R.id.email);
        website = fragmentView.findViewById(R.id.website);
    }
    private void initTaxDeclarations() {
        federalIncome = fragmentView.findViewById(R.id.federal_income);
        stateIncome = fragmentView.findViewById(R.id.state_income);
        socialSecurity = fragmentView.findViewById(R.id.social_security);
        medicareInput = fragmentView.findViewById(R.id.medicare);
        individualRetirement = fragmentView.findViewById(R.id.individual_retirement);
        otherWithholdings = fragmentView.findViewById(R.id.other_withholdings);
    }
    private void initButtonDeclarations() {
        saveButton = fragmentView.findViewById(R.id.save_button);
        cancelButton = fragmentView.findViewById(R.id.cancel_button);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
    private void setTextInputLayoutHint(String hint) {
        TextInputLayout layoutHint;
        layoutHint = fragmentView.findViewById(R.id.text_input_layout);
        layoutHint.setHint(hint);
    }
    @SuppressLint("SetTextI18n")
    private void setViews() {
        jobTitle.setText(jobInformation.getString(getString(R.string.jobTitleKey)));
        completedCheckbox.setChecked(jobInformation.getBoolean(getString(R.string.completedJobKey)));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        payRate.setText(currency.format(jobInformation.getDouble(getString(R.string.payRateKey))));

        addressLine1.setText(jobInformation.getString(getString(R.string.street1Key)));
        addressLine2.setText(jobInformation.getString(getString(R.string.street2Key)));
        city.setText(jobInformation.getString(getString(R.string.cityKey)));
        state.setText(jobInformation.getString(getString(R.string.stateKey)));
        zipcode.setText(jobInformation.getString(getString(R.string.zipCodeKey)));

        phoneNumber.setText(jobInformation.getString(getString(R.string.jobPhoneKey)));
        jobEmail.setText(jobInformation.getString(getString(R.string.jobEmailKey)));
        website.setText(jobInformation.getString(getString(R.string.jobWebsiteKey)));

        federalIncome.setText(Double.toString(
                jobInformation.getDouble(getString(R.string.federalTaxKey))));
        stateIncome.setText(Double.toString(
                jobInformation.getDouble(getString(R.string.stateTaxKey))));
        socialSecurity.setText(Double.toString(
                jobInformation.getDouble(getString(R.string.socialSecurityKey))));
        medicareInput.setText(Double.toString(
                jobInformation.getDouble(getString(R.string.medicareKey))));
        individualRetirement.setText(Double.toString(
                jobInformation.getDouble(getString(R.string.retirementKey))));
        otherWithholdings.setText(Double.toString(
                jobInformation.getDouble(getString(R.string.otherWithholdingsKey))));
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onClick(View view) {
        if (view == saveButton) {
            db = initializeFirestoreInstance();
            saveJob();
        }
        if (view == cancelButton) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
        if (view == calculateRate) {
            openCalculationDialog();
        }
    }

    private void openCalculationDialog() {
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        CalculateSalaryDialog dialog = CalculateSalaryDialog.newInstance(
                Objects.requireNonNull(payRate.getText()).toString());
        dialog.setTargetFragment(this, 0);
        dialog.show(ft, "dialog");
    }

    private FirebaseFirestore db;
    private FirebaseFirestore initializeFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    private void saveJob() {
        if (!validateJobTitleField()) {
            return;
        }
        if (!validatePayRate()) {
            return;
        }

        // Get Job Details from Text Views
        final String job_title = Objects.requireNonNull(jobTitle.getText()).toString().trim();
        final boolean completed_checkbox = completedCheckbox.isChecked();
        DecimalFormat df = new DecimalFormat(".00");
        final String pay_rate = Objects.requireNonNull(payRate.getText()).toString();
        double pay = Double.valueOf(pay_rate.replaceAll("[$,',']", ""));
        pay = Double.valueOf(df.format(pay));

        // Get Address Info from Text Views
        final String street_1 = addressLine1.getText().toString().trim();
        final String street_2 = addressLine2.getText().toString().trim();
        final String city_name = city.getText().toString().trim();
        final String state_name = state.getText().toString().trim();
        final String zip_code = zipcode.getText().toString().trim();

        // Get Job Contact Info from Text Views
        final String phone_number = phoneNumber.getText().toString().trim();
        final String job_email = jobEmail.getText().toString().trim();
        final String web = website.getText().toString().trim();

        // Get Tax Info from Text Views
        String federal_income = Objects.requireNonNull(federalIncome.getText()).toString().trim();
        String state_income = Objects.requireNonNull(stateIncome.getText()).toString().trim();
        String social_security = Objects.requireNonNull(socialSecurity.getText()).toString().trim();
        String medicare = Objects.requireNonNull(medicareInput.getText()).toString().trim();
        String individual_retirement = Objects.requireNonNull(individualRetirement.getText())
                .toString().trim();
        String other_withholdings = Objects.requireNonNull(otherWithholdings.getText())
                .toString().trim();

        final double federalTax;
        final double stateTax;
        final double socialSecurityTax;
        final double medicareTax;
        final double retirementInvestment;
        final double otherWithholdingsInvestment;

        if (!federal_income.isEmpty()) {
            federalTax = Double.valueOf(federal_income);
        } else {
            federalTax = 0;
        }
        if (!state_income.isEmpty()) {
            stateTax = Double.valueOf(state_income);
        } else {
            stateTax = 0;
        }
        if (!social_security.isEmpty()) {
            socialSecurityTax = Double.valueOf(social_security);
        } else {
            socialSecurityTax = 0;
        }
        if (!medicare.isEmpty()) {
            medicareTax = Double.valueOf(medicare);
        } else {
            medicareTax = 0;
        }
        if (!individual_retirement.isEmpty()) {
            retirementInvestment = Double.valueOf(individual_retirement);
        } else {
            retirementInvestment = 0;
        }
        if (!other_withholdings.isEmpty()) {
            otherWithholdingsInvestment = Double.valueOf(other_withholdings);
        } else {
            otherWithholdingsInvestment = 0;
        }

        if (!validatePercentages(federalTax, stateTax, socialSecurityTax, medicareTax,
                retirementInvestment, otherWithholdingsInvestment)) {
            return;
        }

        // Hash map the address
        Map<String, Object> address = new HashMap<>();
        address.put("Street_1", street_1);
        address.put("Street_2", street_2);
        address.put("City", city_name);
        address.put("State", state_name);
        address.put("Zip_Code", zip_code);

        // Hash map that crap
        Map<String, Object> newJob = new HashMap<>();
        newJob.put("Job_Title", job_title);
        newJob.put("Completed", completed_checkbox);
        newJob.put("Pay_Rate", pay);
        newJob.put("Job_Type", jobType);
        newJob.put("Phone", phone_number);
        newJob.put("Email", job_email);
        newJob.put("Website", web);
        newJob.put("Address", address);
        newJob.put("Federal_Income_Tax", federalTax);
        newJob.put("State_Income_Tax", stateTax);
        newJob.put("OASDI", socialSecurityTax);
        newJob.put("Medicare", medicareTax);
        newJob.put("Retirement_Contribution", retirementInvestment);
        newJob.put("Other_Withholdings", otherWithholdingsInvestment);
        if (jobInformation != null) {
            newJob.put("Hours_Worked",
                    jobInformation.getDouble(getString(R.string.hoursWorkedKey)));
        } else {
            newJob.put("Hours_Worked", 0);
        }
        if (completed_checkbox && jobType.equals(getString(R.string.project))) {
            newJob.put("Gross_Pay", pay);
        }
        else if (!jobType.equals(getString(R.string.project))
                && jobInformation != null) {
            newJob.put("Gross_Pay", jobInformation.getDouble(getString(R.string.grossPayKey)));
        }
        else {
            newJob.put("Gross_Pay", 0);
        }

        final double finalPay = pay;
        Log.d(TAG, "saveJob: ");

        CollectionReference jobReference = db.collection("Jobs")
                .document(mainActivity.getUsersEmail())
                .collection("Users_Jobs");

        if (jobInformation != null) {
            Log.d(TAG, "job information was not null: " +
                    jobInformation.getString(getString(R.string.idKey)));
            jobReference.document(Objects.requireNonNull(
                    jobInformation.getString(getString(R.string.idKey))))
                    .update(newJob)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final DocumentReference overviewData = db.collection("Jobs")
                                    .document(mainActivity.getUsersEmail());

                            double passedFinalPay = jobInformation.getDouble(
                                    getString(R.string.payRateKey));
                            boolean passedCompleted = jobInformation.getBoolean(
                                    getString(R.string.completedJobKey));

                            if (passedCompleted && completed_checkbox
                                    && passedFinalPay != finalPay
                                    && jobType.equals(getString(R.string.project))) {
                                overviewData.update("Gross_Pay",
                                        FieldValue.increment(-1 * passedFinalPay))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                overviewData.update("Gross_Pay",
                                                        FieldValue.increment(finalPay));
                                            }
                                        });
                            } else if (passedCompleted && !completed_checkbox) {
                                overviewData.update("Active_Jobs", FieldValue.increment(1));

                                if (jobType.equals(getString(R.string.project))) {
                                    overviewData.update("Gross_Pay",
                                            FieldValue.increment(-1 * passedFinalPay));
                                }
                            } else if (!passedCompleted && completed_checkbox) {
                                overviewData.update("Active_Jobs", FieldValue.increment(-1));

                                if (jobType.equals(getString(R.string.project))) {
                                    overviewData.update("Gross_Pay",
                                            FieldValue.increment(finalPay));
                                }
                            }
                            // If !passedIsComplete && !isComplete -> do nothing

                            // THIS WORKED!
                            Bundle bundle = new Bundle();
                            bundle.putString(getString(R.string.idKey),
                                    jobInformation.getString(getString(R.string.idKey)));
                            bundle.putString(getString(R.string.jobTitleKey) , job_title);
                            bundle.putString(getString(R.string.jobTypeKey) , jobType);
                            bundle.putDouble(getString(R.string.payRateKey), finalPay);
                            bundle.putDouble(getString(R.string.hoursWorkedKey),
                                    jobInformation.getDouble(getString(R.string.hoursWorkedKey)));
                            bundle.putBoolean(getString(R.string.completedJobKey),
                                    completed_checkbox);
                            bundle.putString(getString(R.string.jobEmailKey), job_email);
                            bundle.putDouble(getString(R.string.federalTaxKey), federalTax);
                            bundle.putDouble(getString(R.string.grossPayKey),
                                    jobInformation.getDouble(getString(R.string.grossPayKey)));
                            bundle.putDouble(getString(R.string.medicareKey), medicareTax);
                            bundle.putDouble(getString(R.string.socialSecurityKey),
                                    socialSecurityTax);
                            bundle.putDouble(getString(R.string.otherWithholdingsKey),
                                    otherWithholdingsInvestment);
                            bundle.putString(getString(R.string.jobPhoneKey), phone_number);
                            bundle.putDouble(getString(R.string.retirementKey),
                                    retirementInvestment);
                            bundle.putDouble(getString(R.string.stateTaxKey), stateTax);
                            bundle.putString(getString(R.string.jobWebsiteKey), web);
                            bundle.putString(getString(R.string.street1Key), street_1);
                            bundle.putString(getString(R.string.street2Key), street_2);
                            bundle.putString(getString(R.string.cityKey), city_name);
                            bundle.putString(getString(R.string.stateKey), state_name);
                            bundle.putString(getString(R.string.zipCodeKey), zip_code);

                            Intent intent = new Intent(getContext(), AddJob.class);
                            intent.putExtra("Job Edit Information", bundle);
                            assert getTargetFragment() != null;
                            getTargetFragment().onActivityResult(getTargetRequestCode(),
                                    RESULT_OK, intent);

//                            getFragmentManager().popBackStack();
                            Objects.requireNonNull(getActivity()).onBackPressed();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                }
            });
        } else {
            jobReference.document()
                    .set(newJob)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateUserJobData(finalPay);
                        }
                    });
        }
    }

    // Updates the document containing: Total_Jobs, Active_Jobs and Gross_Pay (overall)
    private void updateUserJobData(final double projectPay) {
        final DocumentReference jobData = db.collection("Jobs")
                .document(mainActivity.getUsersEmail());

        db.collection("Jobs")
                .document(mainActivity.getUsersEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            jobData.update("Total_Jobs", FieldValue.increment(1));
                            if (!completedCheckbox.isChecked()) {
                                jobData.update("Active_Jobs", FieldValue.increment(1));
                            }
                            if (completedCheckbox.isChecked()
                                    && jobType.equals(getString(R.string.project))) {
                                jobData.update("Gross_Pay", FieldValue.increment(projectPay));
                            }
                            Log.d(TAG, "onSuccess: updateUserJobData");
                        }
                        Objects.requireNonNull(getActivity()).onBackPressed();
                    }
                });
    }

    // Form Validations
    private boolean validateJobTitleField() {
        String job = Objects.requireNonNull(jobTitle.getText()).toString().trim();
        // checks if job title text field is empty
        if (TextUtils.isEmpty(job)) {
            jobTitle.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }
    private boolean validatePayRate() {
        String payRateField = Objects.requireNonNull(payRate.getText()).toString().trim();
        // chicks if pay rate text field is empty
        if (TextUtils.isEmpty(payRateField)) {
            payRate.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }
    private boolean validatePercentages(double federal, double state, double social,
                                        double medicare, double retirement, double other) {
        double sum = federal + state + social + medicare + retirement + other;
        if (sum > 100) {
            Toast.makeText(getContext(),
                    "Failed to Create: Percentage total is greater than 100.",
                    Toast.LENGTH_LONG).show();
            TextView taxLabel = fragmentView.findViewById(R.id.taxes_label);
            taxLabel.setError("Percentage total is greater than 100.");
            return false;
        }
        return true;
    }

    public void saveSalaryInfo(double compensation) {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        payRate.setText(currency.format(compensation));
    }
}
