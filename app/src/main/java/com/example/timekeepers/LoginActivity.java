package com.example.timekeepers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Registration.OnFragmentInteractionListener {

    private AutoCompleteTextView emailField;
    private AutoCompleteTextView passwordField;
    private MaterialButton emailSignIn;
    private MaterialButton emailRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Text Views
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        // Initialize Buttons
        emailSignIn = findViewById(R.id.sign_in_button);
        emailRegister = findViewById(R.id.register_button);
        emailSignIn.setOnClickListener(this);
        emailRegister.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.registration_fragment).getVisibility() == View.VISIBLE) {
            findViewById(R.id.registration_fragment).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View view) {
        if (view == emailSignIn) {

        } else if (view == emailRegister) {
            openRegistrationPage();
        }
    }

    private void openRegistrationPage() {
        Fragment fragment = null;
        try {
            fragment = (Registration.class).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction()
                .replace(R.id.registration_fragment, fragment)
                .commit();
        findViewById(R.id.registration_fragment).setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
