package com.example.timekeepers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    // Navigation Drawer Declarations
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    // Persistent clock in status info (survives application closure).
    SharedPreferences sharedPreferences;
    private boolean clockedInStatus;
    private String clockedInJobTitle;
    private String clockedInJobID;
    private long clockInTime;
    private boolean isOnBreak;
    private long beginBreakTime;
    private long endBreakTime;
    private long totalBreakTime;
    private String timerText;

    // Shared Preferences Keys
    private final String STATE_CLOCKIN_STATUS = "state_clockin_status";
    private final String STATE_CLOCKIN_JOB_TITLE = "state_clockin_job_title";
    private final String STATE_CLOCKIN_JOB_ID = "state_clockin_job_id";
    private final String STATE_CLOCKIN_TIME = "state_clockin_time";
    private final String STATE_IS_ON_BREAK = "stateIsOnBreak";
    private final String STATE_BEGIN_BREAK_TIME = "beginBreakTime";
    private final String STATE_END_BREAK_TIME = "endBreakTime";
    private final String STATE_TOTAL_BREAK_TIME = "totalBreakTime";
    private final String STATE_TIMER_TEXT = "stateTimerText";

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

        // Get the clock in status information or set default if not clocked in
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.example.timekeepers", Context.MODE_PRIVATE);
        setClockedInStatus(sharedPreferences.getBoolean(STATE_CLOCKIN_STATUS, false));
        setClockedInJobTitle(sharedPreferences.getString(STATE_CLOCKIN_JOB_TITLE, null));
        setClockedInJobID(sharedPreferences.getString(STATE_CLOCKIN_JOB_ID, null));
        setClockInTime(sharedPreferences.getLong(STATE_CLOCKIN_TIME, 0));
        setIsOnBreak(sharedPreferences.getBoolean(STATE_IS_ON_BREAK, false));
        setBeginBreakTime(sharedPreferences.getLong(STATE_BEGIN_BREAK_TIME, 0L));
        setEndBreakTime(sharedPreferences.getLong(STATE_END_BREAK_TIME, 0L));
        setTotalBreakTime(sharedPreferences.getLong(STATE_TOTAL_BREAK_TIME, 0L));
        setTimerText(sharedPreferences.getString(STATE_TIMER_TEXT, null));

        if (savedInstanceState == null) {
            initializeFirstFragment();
            Log.d("TAG", "onCreate: savedInstanceState=null");
        }
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
            setUsersName(acct.getDisplayName());
            setUsersEmail(acct.getEmail());
            Uri pic = acct.getPhotoUrl();
            Glide.with(this).load(pic).into(usersProfilePicture);

//            initializeFirstFragment();

            // Update db
            UserDBUpdate.updateUserInformation(getUsersName(), getUsersEmail());

            usersNameTextView.setText(getUsersName());
            usersEmailTextView.setText(getUsersEmail());

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
                                setUsersName(doc.getString("Users_Name"));
                                setUsersEmail(doc.getString("Email"));

                                usersNameTextView.setText(getUsersName());
                                usersEmailTextView.setText(getUsersEmail());
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
        Log.d("INITIALIZED STATUS", "Clocked In Status is: " + getClockedInStatus());
        Log.d("INITIALIZED STATUS", "Clocked In Job Title is: " + getClockedInJobTitle());
        Log.d("INITIALIZED STATUS", "Clocked In Job ID is: " + getClockedInJobID());
        Log.d("INITIALIZED STATUS", "Clocked In Job Time is: " + getClockInTime());
        Fragment fragment = Dashboard
                .newInstance(getClockedInStatus(), getClockedInJobID(),
                        getClockedInJobTitle(), getClockInTime(),
                        getIsOnBreak(), getBeginBreakTime(),
                        getEndBreakTime(), getTotalBreakTime(), getTimerText());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_CLOCKIN_STATUS, clockedInStatus);
        outState.putString(STATE_CLOCKIN_JOB_TITLE, clockedInJobTitle);
        outState.putString(STATE_CLOCKIN_JOB_ID, clockedInJobID);
        Log.d("OutState", "onSaveInstanceState: " + clockedInStatus + "..." + clockedInJobTitle + "..." + clockedInJobID);

        super.onSaveInstanceState(outState);
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
                fragment = Dashboard.newInstance(getClockedInStatus(),
                        getClockedInJobID(), getClockedInJobTitle(), getClockInTime(),
                        getIsOnBreak(), getBeginBreakTime(), getEndBreakTime(),
                        getTotalBreakTime(), getTimerText());
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

//        replaceFragment(fragment, fragmentTag);

        assert fragmentTag != null;
        if (!fragmentTag.equals(currentFragmentTag)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            assert fragment != null;
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment, fragmentTag)
                    .commit();
            currentFragmentTag = fragmentTag;
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

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }
    public String getUsersName() {
        return usersName;
    }

    public void setUsersEmail(String usersEmail) {
        this.usersEmail = usersEmail;
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

    public void setClockedInStatus(boolean isClockedIn) {
        this.clockedInStatus = isClockedIn;
        sharedPreferences.edit().putBoolean(STATE_CLOCKIN_STATUS, isClockedIn).apply();
    }
    public boolean getClockedInStatus() {
        return clockedInStatus;
    }
    public void setClockedInJobTitle(String jobTitle) {
        this.clockedInJobTitle = jobTitle;
        sharedPreferences.edit().putString(STATE_CLOCKIN_JOB_TITLE, jobTitle).apply();
    }
    public String getClockedInJobTitle() {
        return clockedInJobTitle;
    }
    public void setClockedInJobID(String jobID) {
        this.clockedInJobID = jobID;
        sharedPreferences.edit().putString(STATE_CLOCKIN_JOB_ID, jobID).apply();
    }
    public String getClockedInJobID() {
        return clockedInJobID;
    }
    public void setClockInTime(long clockInTime) {
        this.clockInTime = clockInTime;
        sharedPreferences.edit().putLong(STATE_CLOCKIN_TIME, clockInTime).apply();
    }
    public long getClockInTime() {
        return clockInTime;
    }
    public void setIsOnBreak(boolean isOnBreak) {
        this.isOnBreak = isOnBreak;
        sharedPreferences.edit().putBoolean(STATE_IS_ON_BREAK, isOnBreak).apply();
    }
    public boolean getIsOnBreak() {
        return isOnBreak;
    }
    public void setBeginBreakTime(long beginBreakTime) {
        this.beginBreakTime = beginBreakTime;
        sharedPreferences.edit().putLong(STATE_BEGIN_BREAK_TIME, beginBreakTime).apply();
    }
    public long getBeginBreakTime() {
        return beginBreakTime;
    }
    public void setEndBreakTime(long endBreakTime) {
        this.endBreakTime = endBreakTime;
        sharedPreferences.edit().putLong(STATE_END_BREAK_TIME, endBreakTime).apply();
    }
    public long getEndBreakTime() {
        return endBreakTime;
    }
    public void setTotalBreakTime(long totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
        sharedPreferences.edit().putLong(STATE_TOTAL_BREAK_TIME, totalBreakTime).apply();
    }
    public long getTotalBreakTime() {
        return totalBreakTime;
    }

    public void setTimerText(String timerText) {
        this.timerText = timerText;
        sharedPreferences.edit().putString(STATE_TIMER_TEXT, timerText).apply();
    }
    public String getTimerText() {
        return timerText;
    }
}
