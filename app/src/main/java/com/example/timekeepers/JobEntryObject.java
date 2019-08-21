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
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipcode;

    public JobEntryObject(String jobId,
                          String jobEntryId,
                          Double breakTime,
                          Date endTime,
                          Double hoursWorked,
                          String jobTitle,
                          String notes,
                          Double pay,
                          Date startTime,
                          String street1,
                          String street2,
                          String city,
                          String state,
                          String zipcode) {
        this.jobId = jobId;
        this.jobEntryId = jobEntryId;
        this.breakTime = breakTime;
        this.endTime = endTime;
        this.hoursWorked = hoursWorked;
        this.jobTitle = jobTitle;
        this.notes = notes;
        this.pay = pay;
        this.startTime = startTime;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
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
    public String getStreet1() {
        return this.street1;
    }
    public String getStreet2() {
        return this.street2;
    }
    public String getCity() {
        return this.city;
    }
    public String getState() {
        return this.state;
    }
    public String getZipcode() {
        return this.zipcode;
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
                "; Start Time: " + this.startTime +
                "; Address: " + this.street1 + " " + this.street2 + " " +
                this.city + " " + this.state + " " + this.zipcode;
    }
}
