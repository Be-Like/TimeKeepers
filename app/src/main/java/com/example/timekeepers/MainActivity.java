package com.example.timekeepers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.timekeepers.Dashboard.Dashboard;
import com.example.timekeepers.JobManagement.CalculateSalaryDialog;
import com.example.timekeepers.Login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timekeepers.JobManagement.JobManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Dashboard.OnFragmentInteractionListener,
        Accounting.OnFragmentInteractionListener, Expenses.OnFragmentInteractionListener,
        Calendar.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener,
        CalculateSalaryDialog.JobCompensationListener {

    public Toolbar toolbar;
    NavigationView navigationView;

    String usersName = "";
    String usersEmail = "";
    TextView usersNameTextView;
    TextView usersEmailTextView;
    ImageView usersProfilePicture;

    // Fragment Tags
    public final String dashboardTag = "DashboardTag";
    public final String jobManagementTag = "JobManagementTag";
    public final String accountingTag = "AccountingTag";
    public final String expensesTag = "ExpensesTag";
    public final String calendarTag = "CalendarTag";
    public final String settingsTag = "SettingsTag";

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_dashboard);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        usersNameTextView = headerView.findViewById(R.id.users_name);
        usersEmailTextView = headerView.findViewById(R.id.users_email);
        usersProfilePicture = headerView.findViewById(R.id.users_profile_picture);


        if (savedInstanceState == null) {
            initializeFirstFragment();
            Log.d("TAG", "onCreate: savedInstanceState=null");
        }

        Log.d("CurrentTAG", "onCreate: " + currentFragmentTag);
        toolbar.setTitle(currentFragmentTag);
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Make sure somebody is logged in
        if (currentUser == null) {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return;
        }

        if (acct != null) {
            usersName = acct.getDisplayName();
            usersEmail = acct.getEmail();
            Uri pic = acct.getPhotoUrl();
            Glide.with(this).load(pic).into(usersProfilePicture);

//            initializeFirstFragment();

            // Update db
            UserDBUpdate.updateUserInformation(usersName, usersEmail);

            usersNameTextView.setText(usersName);
            usersEmailTextView.setText(usersEmail);

        } else {
            // Get user info from db
            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(Objects.requireNonNull(currentUser.getEmail()))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            if (task.isSuccessful()) {
                                assert doc != null;
                                usersName = doc.getString("Users_Name");
                                usersEmail = doc.getString("Email");

                                usersNameTextView.setText(usersName);
                                usersEmailTextView.setText(usersEmail);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error getting user info",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initializeFirstFragment() {
        currentFragmentTag = dashboardTag;
        Fragment fragment = Dashboard.newInstance();
        currentFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }

    private final String KEY_CURRENT_FRAGMENT_TAG = "keyCurrentFragmentTag";
    private Fragment currentFragment;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_CURRENT_FRAGMENT_TAG, currentFragmentTag);
    }

    @Override
    public void onRestoreInstanceState(Bundle restoreSaveState) {
        super.onRestoreInstanceState(restoreSaveState);

        currentFragmentTag = restoreSaveState.getString(KEY_CURRENT_FRAGMENT_TAG);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

String currentFragmentTag;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String fragmentTag = null;

        switch (id) {
            case R.id.nav_dashboard:
                fragment = Dashboard.newInstance();
                fragmentTag = dashboardTag;
                break;
            case R.id.nav_job_management:
                fragment = JobManagement.newInstance();
                fragmentTag = jobManagementTag;
                break;
            case R.id.nav_accounting:
                fragment = Accounting.newInstance();
                fragmentTag = accountingTag;
                break;
            case R.id.nav_expenses:
                fragment = Expenses.newInstance();
                fragmentTag = expensesTag;
                break;
            case R.id.nav_calendar:
                fragment = Calendar.newInstance();
                fragmentTag = calendarTag;
                break;
            case R.id.nav_settings:
                fragment = Settings.newInstance();
                fragmentTag = settingsTag;
        }
        Log.d("TAG", "onNavigationItemSelected: " + id);

        assert fragmentTag != null;
        if (!fragmentTag.equals(currentFragmentTag)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            assert fragment != null;
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment, fragmentTag)
                    .commit();
            currentFragmentTag = fragmentTag;
            currentFragment = fragment;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void saveSalaryInfo(double hourlyRate) {

    }

    public void lockNavigationDrawer(boolean isLocked) {
        if (isLocked) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public String getUsersName() {
        return usersName;
    }

    public String getUsersEmail() {
        return usersEmail;
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
}
