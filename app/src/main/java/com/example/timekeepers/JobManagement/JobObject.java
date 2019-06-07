package com.example.timekeepers.JobManagement;

public class JobObject {
    public final String jobTitle;
    public Double payRate;
    public Double hoursWorked;
    public Boolean completedJob;
    public String jobEmail;
    public Double jobFederal;
    public Double grossPay;
    public Double medicare;
    public Double socialSecurity;
    public Double otherWithholding;
    public String jobPhone;
    public Double retirement;
    public Boolean jobSalary;
    public Boolean jobHourly;
    public Boolean jobProject;
    public Double stateTax;
    public String jobWebsite;
    public String jobStreet1;
    public String jobStreet2;
    public String jobCity;
    public String jobState;
    public String jobZipCode;
    public String generatedJobId;

    public JobObject(String jobTitle,
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

    public String getJobTitle() {
        return jobTitle;
    }

    public String toString() {
        return "Job Title=" + jobTitle +
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
