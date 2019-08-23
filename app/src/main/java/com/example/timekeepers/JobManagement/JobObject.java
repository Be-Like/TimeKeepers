package com.example.timekeepers.JobManagement;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class JobObject implements Serializable {
    private final String jobTitle;
    private final String jobType;
    private final Double payRate;
    private Double jobEntries;
    private Double expenseEntries;
    private final Double hoursWorked;
    private final Boolean completedJob;
    private final String jobEmail;
    private final Double jobFederal;
    private final Double grossPay;
    private final Double medicare;
    private final Double socialSecurity;
    private final Double otherWithholding;
    private final String jobPhone;
    private final Double retirement;
    private final Double stateTax;
    private final String jobWebsite;
    private final String jobStreet1;
    private final String jobStreet2;
    private final String jobCity;
    private final String jobState;
    private final String jobZipCode;
    private final String generatedJobId;

    public JobObject(String jobTitle,
              String jobType,
              Double pay,
              Double jobEntries,
              Double expenseEntries,
              Double hoursWorked,
              Boolean completedJob,
              String jobEmail,
              Double jobFederal,
              Double grossPay,
              Double medicare,
              Double socialSecurity,
              Double otherWithholding,
              String jobPhone,
              Double retirement,
              Double stateTax,
              String jobWebsite,
              String jobStreet1,
              String jobStreet2,
              String jobCity,
              String jobState,
              String jobZipCode,
              String generatedJobId) {
        super();

        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.payRate = pay;
        this.jobEntries = jobEntries;
        this.expenseEntries = expenseEntries;
        this.hoursWorked = hoursWorked;
        this.completedJob = completedJob;
        this.jobEmail = jobEmail;
        this.jobFederal = jobFederal;
        this.grossPay = grossPay;
        this.medicare = medicare;
        this.socialSecurity = socialSecurity;
        this.otherWithholding = otherWithholding;
        this.jobPhone = jobPhone;
        this.retirement = retirement;
        this.stateTax = stateTax;
        this.jobWebsite = jobWebsite;
        this.jobStreet1 = jobStreet1;
        this.jobStreet2 = jobStreet2;
        this.jobCity = jobCity;
        this.jobState = jobState;
        this.jobZipCode = jobZipCode;
        this.generatedJobId = generatedJobId;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public String getJobType() {
        return this.jobType;
    }

    public Double getPayRate() {
        return this.payRate;
    }

    public void setJobEntries(double value) {
        this.jobEntries = value;
    }

    public Double getJobEntries() {
        return this.jobEntries;
    }

    public void setExpenseEntries(double value) {
        this.expenseEntries = value;
    }

    public Double getExpenseEntries() {
        return this.expenseEntries;
    }

    public Double getHoursWorked() {
        return this.hoursWorked;
    }

    public Boolean getCompletedJob() {
        return this.completedJob;
    }

    public String getJobEmail() {
        return this.jobEmail;
    }

    public Double getJobFederal() {
        return this.jobFederal;
    }

    public Double getGrossPay() {
        return this.grossPay;
    }

    public Double getMedicare() {
        return this.medicare;
    }

    public Double getSocialSecurity() {
        return this.socialSecurity;
    }

    public Double getOtherWithholding() {
        return this.otherWithholding;
    }

    public String getJobPhone() {
        return this.jobPhone;
    }

    public Double getRetirement() {
        return this.retirement;
    }

    public Double getStateTax() {
        return this.stateTax;
    }

    public String getJobWebsite() {
        return this.jobWebsite;
    }

    public String getJobStreet1() {
        return this.jobStreet1;
    }

    public String getJobStreet2() {
        return this.jobStreet2;
    }

    public String getJobCity() {
        return this.jobCity;
    }

    public String getJobState() {
        return this.jobState;
    }

    public String getJobZipCode() {
        return this.jobZipCode;
    }

    public String getGeneratedJobId() {
        return this.generatedJobId;
    }

    @NonNull
    public String toString() {
        return "Job Title=" + this.jobTitle +
                " Job Type=" + this.jobType +
                " Completed Job=" + this.completedJob +
                " Pay Rate=" + this.payRate +
                " Gross Pay=" + this.grossPay +
                " Hour Worked=" + this.hoursWorked +
                " Job Phone=" + this.jobPhone +
                " Job Email=" + this.jobEmail +
                " Job Website=" + this.jobWebsite +
                " Job Federal=" + this.jobFederal +
                " State Tax=" + this.stateTax +
                " Social Security=" + this.socialSecurity +
                " Medicare=" + this.medicare +
                " Retirement=" + this.retirement +
                " Other Withholdings=" + this.otherWithholding +
                " Job Street 1=" + this.jobStreet1 +
                " Job Street 2=" + this.jobStreet2 +
                " Job City=" + this.jobCity +
                " Job State=" + this.jobState +
                " Job Zip Code=" + this.jobZipCode +
                " Job Generated Job Id=" + this.generatedJobId;
    }
}
