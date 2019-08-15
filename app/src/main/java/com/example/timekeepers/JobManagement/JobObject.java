package com.example.timekeepers.JobManagement;

import androidx.annotation.NonNull;

public class JobObject {
    private final String jobTitle;
    private final String jobType;
    private final Double payRate;
    private final Double jobEntries;
    private final Double expenseEntries;
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
        return jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public Double getPayRate() {
        return payRate;
    }

    public Double getJobEntries() {
        return jobEntries;
    }

    public Double getExpenseEntries() {
        return expenseEntries;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public Boolean getCompletedJob() {
        return completedJob;
    }

    public String getJobEmail() {
        return jobEmail;
    }

    public Double getJobFederal() {
        return jobFederal;
    }

    public Double getGrossPay() {
        return grossPay;
    }

    public Double getMedicare() {
        return medicare;
    }

    public Double getSocialSecurity() {
        return socialSecurity;
    }

    public Double getOtherWithholding() {
        return otherWithholding;
    }

    public String getJobPhone() {
        return jobPhone;
    }

    public Double getRetirement() {
        return retirement;
    }

    public Double getStateTax() {
        return stateTax;
    }

    public String getJobWebsite() {
        return jobWebsite;
    }

    public String getJobStreet1() {
        return jobStreet1;
    }

    public String getJobStreet2() {
        return jobStreet2;
    }

    public String getJobCity() {
        return jobCity;
    }

    public String getJobState() {
        return jobState;
    }

    public String getJobZipCode() {
        return jobZipCode;
    }

    public String getGeneratedJobId() {
        return generatedJobId;
    }

    @NonNull
    public String toString() {
        return "Job Title=" + jobTitle +
                " Job Type=" + jobType +
                " Completed Job=" + completedJob +
                " Pay Rate=" + payRate +
                " Gross Pay=" + grossPay +
                " Hour Worked=" + hoursWorked +
                " Job Phone=" + jobPhone +
                " Job Email=" + jobEmail +
                " Job Website=" + jobWebsite +
                " Job Federal=" + jobFederal +
                " State Tax=" + stateTax +
                " Social Security=" + socialSecurity +
                " Medicare=" + medicare +
                " Retirement=" + retirement +
                " Other Withholdings=" + otherWithholding +
                " Job Street 1=" + jobStreet1 +
                " Job Street 2=" + jobStreet2 +
                " Job City=" + jobCity +
                " Job State=" + jobState +
                " Job Zip Code=" + jobZipCode +
                " Job Generated Job Id=" + generatedJobId;
    }
}
