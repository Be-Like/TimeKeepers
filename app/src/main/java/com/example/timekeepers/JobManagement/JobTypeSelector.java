package com.example.timekeepers.JobManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.timekeepers.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class JobTypeSelector extends DialogFragment
        implements NavigationView.OnNavigationItemSelectedListener {

    private View dialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogView = inflater.inflate(R.layout.dialog_job_type_selector,
                container, false);

        NavigationView navigationView = dialogView.findViewById(R.id.job_type_selection);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        return dialogView;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        try {
            fragment = (AddJob.class).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (id) {
            case R.id.hourly_option:
                Toast.makeText(getContext(), "Hourly Option Selected", Toast.LENGTH_SHORT).show();
                assert fragment != null;
                ((JobTypeInterface) fragment).jobType("Hourly");
                dismiss();
                break;
            case R.id.salary_option:
                Toast.makeText(getContext(), "Salary Option Selected", Toast.LENGTH_SHORT).show();
                assert fragment != null;
                ((JobTypeInterface) fragment).jobType("Salary");
                dismiss();
                break;
            case R.id.project_option:
                Toast.makeText(getContext(), "Project Option Selected", Toast.LENGTH_SHORT).show();
                assert fragment != null;
                ((JobTypeInterface) fragment).jobType("Project");
                dismiss();
                break;
        }

        FragmentManager fragmentManager =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        assert fragment != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        return true;
    }
}
