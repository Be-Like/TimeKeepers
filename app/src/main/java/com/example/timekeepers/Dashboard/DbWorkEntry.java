package com.example.timekeepers.Dashboard;

import android.annotation.SuppressLint;

import com.example.timekeepers.JobManagement.JobObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DbWorkEntry {

    // Save the work entry to: /Jobs/[user]/Users_Jobs/[jobID]/Job_Entries/[entryID]

    private JobObject jobObject;
    private long startTime;
    private long endTime;
    private double breakTime;
    private String notes;

    /**
     * Items to save:
     *   Job_Title
     *   Start_Time
     *   End_Time
     *   Break_Time
     *   Hours_Worked
     *   Pay
     *   Notes (Eventually)
     */
    public DbWorkEntry(JobObject jobObject, long startTime, long endTime,
                       double breakTime, String notes) {
        this.jobObject = jobObject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakTime = breakTime;
        this.notes = notes;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void saveWorkEntryToDb() {
        double hoursWorked = calculateHoursWorked(startTime, endTime, breakTime);
        double pay;
        if (jobObject.getJobType().equals("Project")) {
            pay = 0.00;
        } else {
            pay = Math.floor((jobObject.getPayRate() * hoursWorked) * 100) / 100;
            updateOverallValues(pay);
        }
        updateJobValues(hoursWorked, pay);

        Map<String, Object> jobEntry = new HashMap<>();
        jobEntry.put("Job_Title", jobObject.getJobTitle());
        jobEntry.put("Start_Time", Objects.requireNonNull(timestampConversion(startTime)));
        jobEntry.put("End_Time", Objects.requireNonNull(timestampConversion(endTime)));
        jobEntry.put("Break_Time", breakTime);
        jobEntry.put("Hours_Worked", calculateHoursWorked(startTime, endTime, breakTime));
        jobEntry.put("Pay", pay);
        jobEntry.put("Notes", notes);

        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobObject.getGeneratedJobId())
                .collection("Job_Entries")
                .document()
                .set(jobEntry);
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
    private Date timestampConversion(long value) {
        String tempString = dateFormat.format(value);
        try {
            return dateFormat.parse(tempString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private double calculateHoursWorked(long startTime, long endTime, double breakTime) {
        double hours = (double) (endTime - startTime) / (60 * 60 * 1000);
        return hours - breakTime;
    }

    private void updateOverallValues(double pay) {
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .update("Gross_pay", FieldValue.increment(pay));
    }
    private void updateJobValues(double hoursWorked, double pay) {
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobObject.getGeneratedJobId())
                .update("Gross_Pay", FieldValue.increment(pay),
                        "Hours_Worked", FieldValue.increment(hoursWorked));
    }
}
