package com.example.timekeepers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.timekeepers.JobManagement.AddJob;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
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
        Dashboard.OnFragmentInteractionListener, JobManagement.OnFragmentInteractionListener,
        Accounting.OnFragmentInteractionListener, Expenses.OnFragmentInteractionListener,
        Calendar.OnFragmentInteractionListener, AddJob.OnFragmentInteractionListener {

    public Toolbar toolbar;

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

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
    }

    public void onStart() {
        super.onStart();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Make sure somebody is logged in
        if (acct == null && currentUser == null) {
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

            initializeFirstFragment();

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

                                initializeFirstFragment();

                                usersNameTextView.setText(usersName);
                                usersEmailTextView.setText(usersEmail);
                            } else {
                                initializeFirstFragment();
                                Toast.makeText(getApplicationContext(),
                                        "Error getting user info",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void initializeFirstFragment() {
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = Dashboard.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        String fragmentTag = null;

        switch (id) {
            case R.id.nav_dashboard:
                fragmentClass = Dashboard.class;
                fragmentTag = dashboardTag;
                break;
            case R.id.nav_job_management:
                fragmentClass = JobManagement.class;
                fragmentTag = jobManagementTag;
                break;
            case R.id.nav_accounting:
                fragmentClass = Accounting.class;
                fragmentTag = accountingTag;
                break;
            case R.id.nav_expenses:
                fragmentClass = Expenses.class;
                fragmentTag = expensesTag;
                break;
            case R.id.nav_calendar:
                fragmentClass = Calendar.class;
                fragmentTag = calendarTag;
                break;
            default:
                break;
        }

        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment, fragmentTag)
                .commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void lockNavigationDrawer(boolean isLocked) {
        if (isLocked) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public String getUsersName() {
        return usersName;
    }

    public String getUsersEmail() {
        return usersEmail;
    }
}
