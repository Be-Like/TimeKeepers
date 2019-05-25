package com.example.timekeepers;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.example.timekeepers.JobManagement.AddJob;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.example.timekeepers.JobManagement.JobManagement;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Dashboard.OnFragmentInteractionListener, JobManagement.OnFragmentInteractionListener,
        Accounting.OnFragmentInteractionListener, Expenses.OnFragmentInteractionListener,
        Calendar.OnFragmentInteractionListener, AddJob.OnFragmentInteractionListener {

    public Toolbar toolbar;

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

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_dashboard);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    // Switch between navigation drawer and back button
//    public void enableNavBar(boolean enabled) {
//
//        if (enabled) {
//            if (!navListenerIsRegistered) {
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//                navListenerIsRegistered = true;
//            }
//            // Calls the onPrepareOptionsMenu method which disables menu items
//            invalidateOptionsMenu();
//
//        }
//        else {
//            // sets the back button to null
//            toggle.setToolbarNavigationClickListener(null);
//            navListenerIsRegistered = false;
//            // Calls the onPrepareOptionsMenu method which enables menu items
//            invalidateOptionsMenu();
//        }
//    }
}
