package com.example.timekeepers.JobManagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;

import java.text.NumberFormat;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobInformation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobInformation extends Fragment {
    private static final String BUNDLE_KEY = "PassedBundle";
    private Bundle jobInformation;
    private OnFragmentInteractionListener mListener;

    // Text View Declarations
    private AppCompatTextView jobTitle;
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
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Toolbar navigation
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .lockNavigationDrawer(true);

        // Instantiate Fragment View
        View fragmentView = inflater.inflate(R.layout.fragment_job_information,
                container, false);

        // Initialize and set views
        initializeViews(fragmentView);
        setViews();

        // Inflate the layout for this fragment
        return fragmentView;
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

    private void initializeViews(View fragmentView) {
        jobTitle = fragmentView.findViewById(R.id.job_title);
        completedJobs = fragmentView.findViewById(R.id.job_status);
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
        NumberFormat currency = NumberFormat.getCurrencyInstance();

        jobTitle.setText(jobInformation.getString("jobTitle"));

        if (jobInformation.getBoolean("completedJob")) {
//            completedJobs.setText(R.string.job_completed);
            completedJobs.setText("Complete");
        } else {
//            completedJobs.setText(R.string.job_incomplete);
            completedJobs.setText("Active");
        }

        payRate.setText(currency.format(jobInformation.getDouble("payRate")));

        // TODO: add job type to the formatting (including the suffix for the pay rate)

        // TODO: add address and address formatting
        addressFormat();

        jobPhone.setText(jobInformation.getString("jobPhone"));
        jobEmail.setText(jobInformation.getString("jobEmail"));
        jobWebsite.setText(jobInformation.getString("jobWebsite"));

        double tmpFederal = jobInformation.getDouble("jobFederal");
        double tmpState = jobInformation.getDouble("stateTax");
        double tmpSocial = jobInformation.getDouble("socialSecurity");
        double tmpMedicare = jobInformation.getDouble("medicare");
        double tmpRetirement = jobInformation.getDouble("retirement");
        double tmpOther = jobInformation.getDouble("otherWithholding");

        jobFederal.setText(tmpFederal + " %");
        stateTax.setText(tmpState + " %");
        socialSecurity.setText(tmpSocial + " %");
        medicare.setText(tmpMedicare + " %");
        retirement.setText(tmpRetirement + " %");
        otherWithholdings.setText(tmpOther + " %");
    }

    @SuppressLint("SetTextI18n")
    private void addressFormat() {
        String passedStreet1 = jobInformation.getString("jobStreet1");
        String passedStreet2 = jobInformation.getString("jobStreet2");
        String passedCity = jobInformation.getString("jobCity");
        String passedState = jobInformation.getString("jobState");
        String passedZipCode = jobInformation.getString("jobZipCode");

        // Assertions
        assert passedStreet1 != null;
        assert passedStreet2 != null;
        assert passedCity != null;
        assert passedState != null;
        assert passedZipCode != null;

        if (!passedStreet1.equals("")) {
            if (!passedStreet2.equals("")) {
                if (!passedState.equals("") && !passedZipCode.equals("")) {
                    if (passedCity.equals("")) {
                        jobAddress.setText(passedStreet1 + "\n" + passedStreet2 +
                                "\n" + passedState + ", " + passedZipCode);
                    } else {
                        jobAddress.setText(passedStreet1 + "\n" + passedStreet2 +
                                "\n" + passedCity + " " + passedState + ", " + passedZipCode);
                    }
                } else {
                    jobAddress.setText(passedStreet1 + "\n" + passedStreet2);
                }
            } else {
                if (!passedState.equals("") && !passedZipCode.equals("")) {
                    if (passedCity.equals("")) {
                        jobAddress.setText(passedStreet1 + "\n" + passedState + ", " + passedZipCode);
                    } else {
                        jobAddress.setText(passedStreet1 + "\n" +
                                passedCity + " " + passedState + ", " + passedZipCode);
                    }
                } else {
                    jobAddress.setText(passedStreet1);
                }
            }
        }
        else if (!passedState.equals("") && !passedZipCode.equals("")) {
            if (passedCity.equals("")) {
                jobAddress.setText(passedState + ", " + passedZipCode);
            } else {
                jobAddress.setText(passedCity + " " + passedState + ", " + passedZipCode);
            }
        }
        else {
            jobAddress.setText("No Address Entered");
        }
    }
}
