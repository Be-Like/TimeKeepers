package com.example.timekeepers.Expenses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeepers.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ExpenseEntryObject> expenseEntry;

    ExpensesAdapter(Context context, ArrayList<ExpenseEntryObject> expenseEntry) {
        this.context = context;
        this.expenseEntry = expenseEntry;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_list_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ExpenseEntryObject entry = expenseEntry.get(position);

        holder.vendor.setText(entry.getExpenseName());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        holder.date.setText(df.format(entry.getExpenseDate()));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        holder.cost.setText(currency.format(entry.getPrice()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Expense: " + entry.toString(),
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "Expense List Entry: " + entry.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseEntry.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView vendor;
        AppCompatTextView date;
        AppCompatTextView cost;
        ConstraintLayout parentLayout;

        ViewHolder(View listItem) {
            super(listItem);
            vendor = listItem.findViewById(R.id.vendor_name);
            date = listItem.findViewById(R.id.expense_date);
            cost = listItem.findViewById(R.id.total_cost);
            parentLayout = listItem.findViewById(R.id.parent_layout);
        }
    }
}
