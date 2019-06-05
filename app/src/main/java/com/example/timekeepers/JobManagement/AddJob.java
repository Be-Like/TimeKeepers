package com.example.timekeepers.JobManagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.timekeepers.CurrencyAdapter;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddJob.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddJob#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddJob extends Fragment
        implements View.OnClickListener {

    // [START] Class Declarations
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private final String addJobTitle = "Add Job";

    // View Declarations
    private View fragmentView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
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
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize fragment view
        fragmentView = inflater.inflate(R.layout.fragment_add_job, container, false);

        // Set Toolbar Title and Lock Navigation Drawer
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .toolbar.setTitle(addJobTitle + " (" + mParam1 + ")");
        ((MainActivity) getActivity()).lockNavigationDrawer(true);

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
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(getContext(), payRate);
        payRate.addTextChangedListener(currencyAdapter);

        if (mParam1.equals(getString(R.string.salary))) {
            setTextInputLayoutHint("Annual Salary");
            AppCompatSpinner payPeriod = fragmentView.findViewById(R.id.pay_period);
            payPeriod.setVisibility(View.VISIBLE);
        } else if (mParam1.equals(getString(R.string.project))) {
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
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
        if (view == cancelButton) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }
}
