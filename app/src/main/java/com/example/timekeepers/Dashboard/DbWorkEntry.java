package com.example.timekeepers.Dashboard;

import android.annotation.SuppressLint;

import com.example.timekeepers.JobEntryObject;
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

    public DbWorkEntry() {
        // Empty constructor
    }
    /**
     * @param jobObject
     * @param startTime
     * @param endTime
     * @param breakTime
     * @param notes
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
        updateJobValues(jobObject.getGeneratedJobId(),hoursWorked, pay);

        Map<String, Object> address = new HashMap<>();
        address.put("Street_1", jobObject.getJobStreet1());
        address.put("Street_2", jobObject.getJobStreet2());
        address.put("City", jobObject.getJobCity());
        address.put("State", jobObject.getJobState());
        address.put("Zip_Code", jobObject.getJobZipCode());

        Map<String, Object> jobEntry = new HashMap<>();
        jobEntry.put("Job_Title", jobObject.getJobTitle());
        jobEntry.put("Start_Time", Objects.requireNonNull(timestampConversion(startTime)));
        jobEntry.put("End_Time", Objects.requireNonNull(timestampConversion(endTime)));
        jobEntry.put("Break_Time", breakTime);
        jobEntry.put("Hours_Worked", calculateHoursWorked(startTime, endTime, breakTime));
        jobEntry.put("Pay", pay);
        jobEntry.put("Notes", notes);
        jobEntry.put("Address", address);

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
                .update("Gross_Pay", FieldValue.increment(pay));
    }
    private void updateJobValues(String jobId, double hoursWorked, double pay) {
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobId)
                .update("Gross_Pay", FieldValue.increment(pay),
                        "Hours_Worked", FieldValue.increment(hoursWorked));
    }
    public void updateJobEntryQuantity(double value) {
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobObject.getGeneratedJobId())
                .update("Quantity_Job_Entries", FieldValue.increment(value));
    }
    public void updateExpenseQuantity(double value) {
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobObject.getGeneratedJobId())
                .update("Quantity_Expense_Entries", FieldValue.increment(value));
    }

    /**
     * @param jobId
     * @param jobEntryId
     * @param startTime
     * @param endTime
     * @param breakTime
     * @param hoursWorked
     * @param pay
     * @param notes
     * @param hoursDifference
     * @param payDifference
     */
    public void editJobEntry(String jobId, String jobEntryId,
                             long startTime, long endTime, double breakTime,
                             double hoursWorked, double pay, String notes,
                             double hoursDifference, double payDifference) {
        // Modifies gross pay and hours worked for overall and job details
        updateOverallValues(payDifference);
        updateJobValues(jobId, hoursDifference, payDifference);

        // Update individual entries (Notes)
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobId)
                .collection("Job_Entries")
                .document(jobEntryId)
                .update("Start_Time", timestampConversion(startTime),
                        "End_Time", timestampConversion(endTime),
                        "Break_Time", breakTime,
                        "Hours_Worked", hoursWorked,
                        "Pay", pay,
                        "Notes", notes);
    }

    public void deleteJobEntry(String entryId, double pay, double hoursWorked) {
        // Update Overall gross pay
        updateOverallValues(-1 * pay);
        // Update Job Details: Job Entries, Gross Pay, Hours Worked
        updateJobValues(jobObject.getGeneratedJobId(), -1 * hoursWorked, -1 * pay);

        // Delete Job Entry
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(jobObject.getGeneratedJobId())
                .collection("Job_Entries")
                .document(entryId)
                .delete();
    }
}
