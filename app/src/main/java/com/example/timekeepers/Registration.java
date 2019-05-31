package com.example.timekeepers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.concurrent.Executor;

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

    // [START]Firebase Declaration
    private FirebaseAuth userAuth;
    // [END]Firebase Declaration

    // Passed Parameters
    private String email;
    private String password;

    // Fragment Listener
    private OnFragmentInteractionListener mListener;

    // View Declarations
    private View fragmentView;
    private TextInputEditText firstNameField;
    private TextInputEditText lastNameField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private TextInputEditText passwordConfirmationField;
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

        userAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize Fragment View
        fragmentView = inflater.inflate(R.layout.fragment_registration, container, false);

        // Text Field Initializers
        firstNameField = fragmentView.findViewById(R.id.first_name);
        lastNameField = fragmentView.findViewById(R.id.last_name);
        emailField = fragmentView.findViewById(R.id.email);
        passwordField = fragmentView.findViewById(R.id.password);
        passwordConfirmationField =
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
            registerUser();
        } else if (v == cancelButton) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
    }

    private void registerUser() {
        if (!((LoginActivity) Objects.requireNonNull(getActivity())).validateEmailField(emailField)) {
            return;
        }

        if (!((LoginActivity) getActivity()).validatePasswordField(passwordField)) {
            return;
        }

        ((LoginActivity) getActivity()).showProgress(true);

        email = emailField.getText().toString().trim();
        password = passwordField.getText().toString().trim();

        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()),
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.valid_registration,
                                    Toast.LENGTH_SHORT).show();
                            startActivity(
                                    ((LoginActivity) Objects.requireNonNull(getActivity()))
                                            .startMainActivity()
                            );
                            ((LoginActivity) getActivity()).showProgress(false);
                        } else {
                            ((LoginActivity) getActivity()).showProgress(false);
                            Toast.makeText(getContext(),
                                    "Failed to Register: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
