package com.example.timekeepers.Expenses;

public class ExpenseEntryObject {

    private String jobId;
    private String expenseEntryId;
    private String jobTitle;

    public ExpenseEntryObject(String jobId,
                              String expenseEntryId,
                              String jobTitle) {
        this.jobId = jobId;
        this.expenseEntryId = expenseEntryId;
        this.jobTitle = jobTitle;
    }

    public String getJobId() {
        return this.jobId;
    }
    public String getExpenseEntryId() {
        return this.expenseEntryId;
    }
    public String getJobTitle() {
        return this.jobTitle;
    }

    public String toString() {
        return "";
    }

}
