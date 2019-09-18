package com.example.timekeepers;

import com.example.timekeepers.Expenses.ExpenseEntryObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DBExpenseEntry {

    private ExpenseEntryObject expenseEntryObject;

    public DBExpenseEntry(ExpenseEntryObject expenseEntryObject) {
        this.expenseEntryObject = expenseEntryObject;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void saveNewExpenseEntry() {
        Map<String, Object> address = new HashMap<>();
        address.put("Street_1", expenseEntryObject.getStreet1());
        address.put("Street_2", expenseEntryObject.getStreet2());
        address.put("City", expenseEntryObject.getCity());
        address.put("State", expenseEntryObject.getExpenseState());
        address.put("Zip_Code", expenseEntryObject.getZipcode());

        Map<String, Object> expenseEntry = new HashMap<>();
        expenseEntry.put("Job_Title", expenseEntryObject.getJobTitle());
        expenseEntry.put("Vendor", expenseEntryObject.getExpenseName());
        expenseEntry.put("Expense_Category", expenseEntryObject.getCategory());
        expenseEntry.put("Date", expenseEntryObject.getExpenseDate());
        expenseEntry.put("Total_Cost", expenseEntryObject.getPrice());
        expenseEntry.put("Address", address);

        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(Objects.requireNonNull(getCurrentUser().getEmail()))
                .collection("Users_Jobs")
                .document(expenseEntryObject.getJobId())
                .collection("Expense_Entries")
                .document()
                .set(expenseEntry);
    }

}
