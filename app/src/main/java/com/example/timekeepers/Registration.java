package com.example.timekeepers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Registration.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Registration#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registration extends Fragment
        implements View.OnClickListener {
    // the fragment initialization parameters
    private static final String EMAIL_PARAM = "emailParam";
    private static final String PASSWORD_PARAM= "passwordParam";

    // Passed Parameters
    private String email;
    private String password;

    // Fragment Listener
    private OnFragmentInteractionListener mListener;

    // View Declarations
    private View fragmentView;
    private MaterialButton registerButton;
    private MaterialButton cancelButton;

    public Registration() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param emailParam Parameter 1.
     * @param passwordParam Parameter 2.
     * @return A new instance of fragment Registration.
     */
    public static Registration newInstance(String emailParam, String passwordParam) {
        Registration fragment = new Registration();
        Bundle args = new Bundle();
        args.putString(EMAIL_PARAM, emailParam);
        args.putString(PASSWORD_PARAM, passwordParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL_PARAM);
            password = getArguments().getString(PASSWORD_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize Fragment View
        fragmentView = inflater.inflate(R.layout.fragment_registration, container, false);

        // Text Field Initializers
        TextInputEditText firstNameField = fragmentView.findViewById(R.id.first_name);
        TextInputEditText lastNameField = fragmentView.findViewById(R.id.last_name);
        TextInputEditText emailField = fragmentView.findViewById(R.id.email);
        TextInputEditText passwordField = fragmentView.findViewById(R.id.password);
        TextInputEditText passwordConfirmationField =
                fragmentView.findViewById(R.id.password_confirmation);

        // Set Text Fields that were passed in
        emailField.setText(email);
        passwordField.setText(password);

        // Button Initializers
        registerButton = fragmentView.findViewById(R.id.register_button);
        cancelButton = fragmentView.findViewById(R.id.cancel_button);
        registerButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

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

    public void onClick(View v) {
        if (v == registerButton) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        } else if (v == cancelButton) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }
}
