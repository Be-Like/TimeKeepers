package com.example.timekeepers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Registration.OnFragmentInteractionListener {

    // [START]Firebase Declaration
    private FirebaseAuth userAuth;
    private GoogleSignInClient mGoogleSignInClient;
    // [END]Firebase Declaration

    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private MaterialButton emailSignIn;
    private MaterialButton emailRegister;
    private SignInButton googleSignIn;

    @Override
    public void onStart() {
        super.onStart();
        // TODO: uncomment and add transition to go to main activity vvv
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // TODO: Get rid of these ^^^vvv
        userAuth.signOut();
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        GoogleSignIn.getClient(this, gso).signOut();
        Log.d(TAG, "onStart: Logged Out");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Text Views
        // TODO: delete this
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        // Initialize Buttons
        emailSignIn = findViewById(R.id.sign_in_button);
        emailRegister = findViewById(R.id.register_button);
        emailSignIn.setOnClickListener(this);
        emailRegister.setOnClickListener(this);
        googleSignIn = findViewById(R.id.google_sign_in);
        googleSignIn.setSize(SignInButton.SIZE_ICON_ONLY);
        googleSignIn.setOnClickListener(this);
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
        } else if (view == googleSignIn) {
            googleSignIn();
        }
    }

private String TAG = "Sign In - ";
    private void emailSignIn(){
        // Validate the email and password
        if (!validateEmailField(emailField)) {
            return;
        }
        if (!validatePasswordField(passwordField)) {
            return;
        }

        showProgress(true);

        String email = Objects.requireNonNull(emailField.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordField.getText()).toString().trim();

        userAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                            startActivity(startMainActivity());
                            showProgress(false);
                        } else {
                            showProgress(false);
                            Log.d(TAG, "onComplete: failure");
                        }
                    }
                });
    }

    private void openRegistrationPage() {
        String email = Objects.requireNonNull(emailField.getText()).toString();
        String password = Objects.requireNonNull(passwordField.getText()).toString();

        Fragment fragment = Registration
                .newInstance(email, password);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.registration_fragment, fragment)
                .commit();
        findViewById(R.id.registration_fragment).setVisibility(View.VISIBLE);
    }

    private int RC_SIGN_IN = 1995;
    private void googleSignIn() {
        showProgress(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                showProgress(false);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(startMainActivity());
                            showProgress(false);
                        } else {
                            showProgress(false);
                            Toast.makeText(getApplicationContext(),
                                    "Failed to sign in: " +
                                            Objects.requireNonNull(task.getException())
                                                    .getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Validate text fields
    public boolean validateEmailField(TextInputEditText field) {
        String email = Objects.requireNonNull(field.getText()).toString().trim();
        // checks if email text field is empty
        if (TextUtils.isEmpty(email)) {
            field.setError(getString(R.string.error_field_required));
            return false;
        }
        if (!isValidEmailAddress(email)) {
            field.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }
    private boolean isValidEmailAddress(String emailAddress) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }
    public boolean validatePasswordField(TextInputEditText field) {
        String password = Objects.requireNonNull(field.getText()).toString().trim();
        if (TextUtils.isEmpty(password)) {
            field.setError(getString(R.string.error_field_required));
            return false;
        }
        if (password.length() < 6) {
            field.setError(getString(R.string.password_too_short));
            return false;
        }
        if (!passwordContentValidation(password)) {
            field.setError(getString(R.string.password_contains));
            return false;
        }
        return true;
    }
    private boolean passwordContentValidation(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String password_pattern =
                "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).*";
        pattern = Pattern.compile(password_pattern);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void showProgress(boolean showProgress) {
        FrameLayout progressBar = findViewById(R.id.progress_bar);
        AlphaAnimation inAnimation;
        AlphaAnimation outAnimation;

        if (showProgress) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            inAnimation = new AlphaAnimation(0f,1f);
            inAnimation.setDuration(200);
            progressBar.setAnimation(inAnimation);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBar.setAnimation(outAnimation);
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams
                    .FLAG_NOT_TOUCHABLE);
        }
    }

    public Intent startMainActivity() {
        finish();
        return new Intent(getApplicationContext(), MainActivity.class);
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
