package com.example.timekeepers.JobManagement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeepers.CurrencyTextListener;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CalculateSalaryDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculateSalaryDialog extends DialogFragment
        implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    private String passedCompensation;

    private JobCompensationListener mListener;

    private TextInputEditText annualSalary;
    private TextInputEditText hours;
    private TextInputEditText weeks;

    public CalculateSalaryDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CalculateSalaryDialog.
     */
    public static CalculateSalaryDialog newInstance(String param1) {
        CalculateSalaryDialog fragment = new CalculateSalaryDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            passedCompensation = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_calculate_salary_dialog,
                container, false);

        annualSalary = dialogView.findViewById(R.id.compensation);
        annualSalary.setText(passedCompensation);
        CurrencyTextListener currencyTextListener = new CurrencyTextListener(annualSalary);
        annualSalary.addTextChangedListener(currencyTextListener);

        hours = dialogView.findViewById(R.id.weekly_hours);
        weeks = dialogView.findViewById(R.id.annual_weeks_worked);

        MaterialButton saveButton = dialogView.findViewById(R.id.save_button);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancel_button);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return dialogView;
    }

    public void onButtonPressed(Double value) {
        if (mListener != null) {
            mListener.saveSalaryInfo(value);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof JobCompensationListener) {
            mListener = (JobCompensationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement JobCompensationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                calculateSalaryToHourly();
                break;

            case R.id.cancel_button:
                Objects.requireNonNull(getActivity()).onBackPressed();
                break;
        }
    }

    // Calculate Salary to Hourly
    private void calculateSalaryToHourly() {
        if (!validateEntries()) {
            return;
        }
        final String annual_salary = Objects.requireNonNull(annualSalary.getText()).toString();

        final double salary = Double.valueOf(annual_salary.replaceAll("[$, ',']", ""));
        final double avgHours = Double.valueOf(Objects.requireNonNull(hours.getText()).toString());
        final double avgWeeks = Double.valueOf(Objects.requireNonNull(weeks.getText()).toString());

        final double avgHourlyRate = (salary / avgWeeks) / avgHours;
        Log.d(TAG, "calculateSalaryToHourly: " + avgHourlyRate);

        assert getTargetFragment() != null;
        ((JobCompensationListener) getTargetFragment()).saveSalaryInfo(avgHourlyRate);
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    private boolean validateEntries() {
        final String annual_salary = Objects.requireNonNull(annualSalary.getText()).toString();
        final String average_hours = Objects.requireNonNull(hours.getText()).toString();
        final String average_weeks = Objects.requireNonNull(weeks.getText()).toString();

        if (TextUtils.isEmpty(annual_salary)) {
            annualSalary.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(average_hours)) {
            hours.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(average_weeks)) {
            weeks.setError(getString(R.string.error_field_required));
            return false;
        }

        final double avgHours = Double.valueOf(Objects.requireNonNull(hours.getText()).toString());
        final double avgWeeks = Double.valueOf(Objects.requireNonNull(weeks.getText()).toString());

        if (avgHours <= 0) {
            hours.setError(getString(R.string.greater_than_0));
            return false;
        }
        if (avgWeeks <= 0) {
            weeks.setError(getString(R.string.greater_than_0));
            return false;
        }

        return true;
    }

    public interface JobCompensationListener {
        void saveSalaryInfo(double hourlyRate);
    }

}
