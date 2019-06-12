package com.example.timekeepers.JobManagement;

import androidx.annotation.NonNull;

public class JobObject {
    private final String jobTitle;
    private final String jobType;
    private final Double payRate;
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

    JobObject(String jobTitle,
              String jobType,
              Double pay,
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

    String getJobTitle() {
        return jobTitle;
    }

    String getJobType() {
        return jobType;
    }

    Double getPayRate() {
        return payRate;
    }

    Double getHoursWorked() {
        return hoursWorked;
    }

    Boolean getCompletedJob() {
        return completedJob;
    }

    String getJobEmail() {
        return jobEmail;
    }

    Double getJobFederal() {
        return jobFederal;
    }

    Double getGrossPay() {
        return grossPay;
    }

    Double getMedicare() {
        return medicare;
    }

    Double getSocialSecurity() {
        return socialSecurity;
    }

    Double getOtherWithholding() {
        return otherWithholding;
    }

    String getJobPhone() {
        return jobPhone;
    }

    Double getRetirement() {
        return retirement;
    }

    Double getStateTax() {
        return stateTax;
    }

    String getJobWebsite() {
        return jobWebsite;
    }

    String getJobStreet1() {
        return jobStreet1;
    }

    String getJobStreet2() {
        return jobStreet2;
    }

    String getJobCity() {
        return jobCity;
    }

    String getJobState() {
        return jobState;
    }

    String getJobZipCode() {
        return jobZipCode;
    }

    String getGeneratedJobId() {
        return jobZipCode;
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
