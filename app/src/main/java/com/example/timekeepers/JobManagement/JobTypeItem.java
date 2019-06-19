package com.example.timekeepers.JobManagement;

import androidx.annotation.NonNull;

public class JobTypeItem {
    private final String jobType;
    final int jobTypeIcon;

    JobTypeItem(String jobType, int jobTypeIcon) {
        this.jobType = jobType;
        this.jobTypeIcon = jobTypeIcon;
    }

    @NonNull
    @Override
    public String toString() {
        return jobType;
    }
}
