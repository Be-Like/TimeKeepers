package com.example.timekeepers.JobManagement;

// TODO: return JobTypItem to public along with two variable names (jobType and jobTypeIcon)
//  if broken
public class JobTypeItem {
    private final String jobType;
    final int jobTypeIcon;

    JobTypeItem(String jobType, int jobTypeIcon) {
        this.jobType = jobType;
        this.jobTypeIcon = jobTypeIcon;
    }

    @Override
    public String toString() {
        return jobType;
    }
}
