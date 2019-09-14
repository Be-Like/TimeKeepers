package com.example.timekeepers.Expenses;

import java.util.Date;

public class ExpenseEntryObject {

    private String jobId;
    private String expenseEntryId;
    private String jobTitle;
    private String expenseName;
    private String category;
    private Date expenseDate;
    private Double price;
    private String street1;
    private String street2;
    private String city;
    private String expenseState;
    private String zipcode;


    public ExpenseEntryObject(String jobId,
                              String expenseEntryId,
                              String jobTitle,
                              String expenseName,
                              String category,
                              Date expenseDate,
                              Double price,
                              String street1,
                              String street2,
                              String city,
                              String expenseState,
                              String zipcode) {
        this.jobId = jobId;
        this.expenseEntryId = expenseEntryId;
        this.jobTitle = jobTitle;
        this.expenseName = expenseName;
        this.category = category;
        this.expenseDate = expenseDate;
        this.price = price;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.expenseState = expenseState;
        this.zipcode = zipcode;
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
    public String getExpenseName() {
        return this.expenseName;
    }
    public String getCategory() {
        return this.category;
    }
    public Date getExpenseDate() {
        return this.expenseDate;
    }
    public Double getPrice() {
        return this.price;
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
    public String getExpenseState() {
        return this.expenseState;
    }
    public String getZipcode() {
        return this.zipcode;
    }

    public String toString() {
        return "Job ID: " + this.jobId +
                "Expense Entry Id: " + this.expenseEntryId +
                "Job Title: " + this.jobTitle +
                "Expense Name: " + this.expenseName +
                "Category: " + this.category +
                "Date: " + this.expenseDate +
                "Price: " + this.price +
                "Address: " + this.street1 + this.street2 +
                this.city + this.expenseState + this.zipcode;
    }

}
