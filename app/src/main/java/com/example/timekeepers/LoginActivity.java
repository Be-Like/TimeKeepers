package com.example.timekeepers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Registration.OnFragmentInteractionListener {

    // [START]Firebase Declaration
    private FirebaseAuth userAuth;
    // [END]Firebase Declaration

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private MaterialButton emailSignIn;
    private MaterialButton emailRegister;

    @Override
    public void onStart() {
        super.onStart();
        userAuth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();

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
            emailSignIn();
        } else if (view == emailRegister) {
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
            openRegistrationPage();
        }
    }
private String TAG = "Sign In - ";
    private void emailSignIn(){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        userAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                        } else {
                            Log.d(TAG, "onComplete: failure");
                        }
                    }
                });
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

    // Keyboard dismissal logic
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
