package com.example.timekeepers;

import java.util.Date;

public class JobEntryObject {

    private String jobId;
    private String jobEntryId;
    private Double breakTime;
    private Date endTime;
    private Double hoursWorked;
    private String jobTitle;
    private String notes;
    private Double pay;
    private Date startTime;

    public JobEntryObject(String jobId,
                          String jobEntryId,
                          Double breakTime,
                          Date endTime,
                          Double hoursWorked,
                          String jobTitle,
                          String notes,
                          Double pay,
                          Date startTime) {
        this.jobId = jobId;
        this.jobEntryId = jobEntryId;
        this.breakTime = breakTime;
        this.endTime = endTime;
        this.hoursWorked = hoursWorked;
        this.jobTitle = jobTitle;
        this.notes = notes;
        this.pay = pay;
        this.startTime = startTime;
    }

    public String getJobId() {
        return this.jobId;
    }
    public String getJobEntryId() {
        return this.jobEntryId;
    }
    public Double getBreakTime() {
        return this.breakTime;
    }
    public Date getEndTime() {
        return this.endTime;
    }
    public Double getHoursWorked() {
        return this.hoursWorked;
    }
    public String getJobTitle() {
        return this.jobTitle;
    }
    public String getNotes() {
        return this.notes;
    }
    public Double getPay() {
        return this.pay;
    }
    public Date getStartTime() {
        return this.startTime;
    }

    public String toString() {
        return "Job Id: " + this.jobId +
                "Job Entry Id: " + this.jobEntryId +
                "; Break Time: " + this.breakTime +
                "; End Time: " + this.endTime +
                "; Hours Worked: " + this.hoursWorked +
                "; Job Title: " + this.jobTitle +
                "; Notes: " + this.notes +
                "; Pay: " + this.pay +
                "; Start Time: " + this.startTime;
    }
}
