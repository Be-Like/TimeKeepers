package com.example.timekeepers.JobManagement;

public class JobTypeItem {
    public final String jobType;
    public final int jobTypeIcon;

    public JobTypeItem(String jobType, int jobTypeIcon) {
        this.jobType = jobType;
        this.jobTypeIcon = jobTypeIcon;
    }

    @Override
    public String toString() {
        return jobType;
    }
}
