package com.example.timekeepers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Registration.OnFragmentInteractionListener {

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
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
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
            openRegistrationPage();
        }
    }

    private void openRegistrationPage() {
        Fragment fragment = Registration.newInstance(Objects.requireNonNull(emailField.getText()).toString(),
                Objects.requireNonNull(passwordField.getText()).toString());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.registration_fragment, fragment)
                .commit();
        findViewById(R.id.registration_fragment).setVisibility(View.VISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();

        if (v != null &&
                (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof TextInputEditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] scrcoords = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + v.getLeft() - scrcoords[0];
            float y = event.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(event);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm =
                    (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
