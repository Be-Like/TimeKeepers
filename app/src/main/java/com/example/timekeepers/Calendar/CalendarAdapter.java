package com.example.timekeepers.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
                Bundle bundle = new Bundle();
                bundle.putString(context.getString(R.string.idKey), entry.getJobId());
                bundle.putString(context.getString(R.string.jobEntryIdKey), entry.getJobEntryId());
                bundle.putDouble(context.getString(R.string.breakTimeKey), entry.getBreakTime());
                bundle.putSerializable(context.getString(R.string.endTimeKey), entry.getEndTime());
                bundle.putDouble(context.getString(R.string.hoursWorkedKey), entry.getHoursWorked());
                bundle.putString(context.getString(R.string.jobTitleKey), entry.getJobTitle());
                bundle.putString(context.getString(R.string.notesKey), entry.getNotes());
                bundle.putDouble(context.getString(R.string.payKey), entry.getPay());
                bundle.putSerializable(context.getString(R.string.startTimeKey),
                        entry.getStartTime());
                bundle.putString(context.getString(R.string.street1Key), entry.getStreet1());
                bundle.putString(context.getString(R.string.street2Key), entry.getStreet2());
                bundle.putString(context.getString(R.string.cityKey), entry.getCity());
                bundle.putString(context.getString(R.string.stateKey), entry.getState());
                bundle.putString(context.getString(R.string.zipCodeKey), entry.getZipcode());


                Fragment viewJobEntry = JobEntryInfo.newInstance(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, viewJobEntry)
                        .addToBackStack(null)
                        .commit();
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
