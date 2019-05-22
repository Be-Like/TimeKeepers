package com.example.timekeepers.JobManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.timekeepers.R;
import com.google.android.material.navigation.NavigationView;

public class JobTypeSelector extends DialogFragment
        implements NavigationView.OnNavigationItemSelectedListener {

    private View dialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogView = inflater.inflate(R.layout.dialog_job_type_selector,
                container, false);

        NavigationView navigationView = dialogView.findViewById(R.id.job_type_selection);
        navigationView.setNavigationItemSelectedListener(this);

        return dialogView;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.hourly_option:
                Toast.makeText(getContext(), "Hourly Option Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.salary_option:
                Toast.makeText(getContext(), "Salary Option Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.project_option:
                Toast.makeText(getContext(), "Project Option Selected", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
