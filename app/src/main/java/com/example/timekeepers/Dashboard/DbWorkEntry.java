package com.example.timekeepers.Dashboard;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void saveWorkEntryToDb() {
        double hoursWorked = calculateHoursWorked(startTime, endTime, breakTime);
        double pay;
        if (jobObject.getJobType().equals("Project")) {
            pay = 0.00;
        } else {
            pay = Math.floor((jobObject.getPayRate() * hoursWorked) * 100) / 100;
        }

        Map<String, Object> jobEntry = new HashMap<>();
        jobEntry.put("Job_Title", jobObject.getJobTitle());
        jobEntry.put("Start_Time", timestampConversion(startTime));
        jobEntry.put("End_Time", timestampConversion(endTime));
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
        // TODO: Remember to update:
        //  Job Hours and Gross Pay
        //  Overall Hours and Gross Pay
    }

    @SuppressLint("SimpleDateFormat")
    static final SimpleDateFormat dateFormat =
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

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
