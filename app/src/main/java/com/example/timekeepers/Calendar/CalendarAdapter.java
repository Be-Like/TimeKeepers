package com.example.timekeepers.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeepers.JobEntryObject;
import com.example.timekeepers.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private Context context;
    private ArrayList<JobEntryObject> calendarEntry;

    CalendarAdapter(Context context, ArrayList<JobEntryObject> calendarEntry) {
        this.context = context;
        this.calendarEntry = calendarEntry;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final JobEntryObject entry = calendarEntry.get(position);

        holder.title.setText(entry.getJobTitle());
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        holder.pay.setText(currency.format(entry.getPay()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Will open " + entry.getJobTitle() + " entry.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return calendarEntry.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView title;
        AppCompatTextView pay;
        ConstraintLayout parentLayout;

        ViewHolder(View listItem) {
            super(listItem);
            title = listItem.findViewById(R.id.title);
            pay = listItem.findViewById(R.id.pay);
            parentLayout = listItem.findViewById(R.id.parent_layout);
        }
    }

}
