package com.example.timekeepers.JobManagement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.timekeepers.CurrencyTextListener;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // [START] Class Declarations
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String addJobTitle = "Add Job";

    private MainActivity mainActivity;

    // View Declarations
    private View fragmentView;

    private String jobType;

    // Job Details Declarations
    private TextInputEditText jobTitle;
    private CheckBox completedCheckbox;
    private TextInputEditText payRate;
    private String payPeriod;

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
    private Button saveButton;
    private Button cancelButton;

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
    // TODO: Rename and change types and number of parameters
    public static AddJob newInstance(String jobType) {
        AddJob fragment = new AddJob();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jobType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobType = getArguments().getString(ARG_PARAM1);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize fragment view
        fragmentView = inflater.inflate(R.layout.fragment_add_job, container, false);

        // Set Toolbar Title and Lock Navigation Drawer
        mainActivity.toolbar.setTitle(addJobTitle + " (" + jobType + ")");
        mainActivity.lockNavigationDrawer(true);

        // Initialize form declarations
        initJobDetailDeclarations();
        initAddressDeclarations();
        initJobContactDeclarations();
        initTaxDeclarations();
        initButtonDeclarations();

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
            setTextInputLayoutHint("Annual Salary");
            AppCompatSpinner payPeriod = fragmentView.findViewById(R.id.pay_period);
            ArrayAdapter<CharSequence> adapter =
                    ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                            R.array.pay_period_selections,
                            android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            payPeriod.setAdapter(adapter);
            payPeriod.setVisibility(View.VISIBLE);
            payPeriod.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        payPeriod = parent.getItemAtPosition(pos).toString();
    }
    public void onNothingSelected(AdapterView<?> parent) {}

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
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
        String job_title = Objects.requireNonNull(jobTitle.getText()).toString().trim();
        Boolean completed_checkbox = completedCheckbox.isChecked();
        DecimalFormat df = new DecimalFormat(".00");
        String pay_rate = Objects.requireNonNull(payRate.getText()).toString();
        double pay = Double.valueOf(pay_rate.replaceAll("[$,',']", ""));
        pay = Double.valueOf(df.format(pay));

        // Get Address Info from Text Views
        String street_1 = addressLine1.getText().toString().trim();
        String street_2 = addressLine2.getText().toString().trim();
        String city_name = city.getText().toString().trim();
        String state_name = state.getText().toString().trim();
        String zip_code = zipcode.getText().toString().trim();

        // Get Job Contact Info from Text Views
        String phone_number = phoneNumber.getText().toString().trim();
        String job_email = jobEmail.getText().toString().trim();
        String web = website.getText().toString().trim();

        // Get Tax Info from Text Views
        String federal_income = federalIncome.getText().toString().trim();
        String state_income = stateIncome.getText().toString().trim();
        String social_security = socialSecurity.getText().toString().trim();
        String medicare = medicareInput.getText().toString().trim();
        String individual_retirement = individualRetirement.getText().toString().trim();
        String other_withholdings = otherWithholdings.getText().toString().trim();

        // Hash map the address
        Map<String, Object> address = new HashMap<>();
        address.put("Street_1", street_1);
        address.put("Street_2", street_2);
        address.put("City", city_name);
        address.put("State", state_name);
        address.put("Zip_Code", zip_code);
        // TODO: Figure out how to implement salary pay... (spinners, or something).

        // Hash map that crap
        Map<String, Object> newJob = new HashMap<>();
        newJob.put("Job_Title", job_title);
        newJob.put("Completed", completed_checkbox);
        newJob.put("Pay_Rate", pay);
        newJob.put("Pay_Period", payPeriod);
        newJob.put("Job_Type", jobType);
        newJob.put("Phone", phone_number);
        newJob.put("Email", job_email);
        newJob.put("Website", web);
        newJob.put("Hours_Worked", 0);
        newJob.put("Address", address);
        if (completed_checkbox && jobType.equals(getString(R.string.project))) {
            newJob.put("Gross_Pay", pay);
        } else {
            newJob.put("Gross_Pay", 0);
        }


        // Tax and Withholding Information
        if (!federal_income.isEmpty()) {
            newJob.put("Federal_Income_Tax", Double.valueOf(federal_income));
        } else {
            newJob.put("Federal_Income_Tax", 0);
        }
        if (!state_income.isEmpty()) {
            newJob.put("State_Income_Tax", Double.valueOf(state_income));
        } else {
            newJob.put("State_Income_Tax", 0);
        }
        if (!social_security.isEmpty()) {
            newJob.put("OASDI", Double.valueOf(social_security));
        } else {
            newJob.put("OASDI", 0);
        }
        if (!medicare.isEmpty()) {
            newJob.put("Medicare", Double.valueOf(medicare));
        } else {
            newJob.put("Medicare", 0);
        }
        if (!individual_retirement.isEmpty()) {
            newJob.put("Retirement_Contribution", Double.valueOf(individual_retirement));
        } else {
            newJob.put("Retirement_Contribution", 0);
        }
        if (!other_withholdings.isEmpty()) {
            newJob.put("Other_Withholdings", Double.valueOf(other_withholdings));
        } else {
            newJob.put("Other_Withholdings", 0);
        }

        final double grossPay = pay;
        Log.d(TAG, "saveJob: ");
        db.collection("Jobs").document(mainActivity.getUsersEmail())
                .collection("Users_Jobs")
                .document()
                .set(newJob)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateUserJobData(grossPay);
                    }
                });
    }

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
                            // Go back to Job Management page
                            Objects.requireNonNull(getActivity()).onBackPressed();
                        }
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
}
