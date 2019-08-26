package com.example.timekeepers.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeepers.Accounting;
import com.example.timekeepers.AddressFormat;
import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>  {
    private ArrayList<JobObject> jobs;
    private Context context;
    private FragmentActivity frag;
    ClockInListener listener;

    DashboardAdapter(Context context, ArrayList<JobObject> jobs,
                     FragmentActivity frag, ClockInListener listener) {
        this.jobs = jobs;
        this.context = context;
        this.frag = frag;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_card_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final JobObject entry = jobs.get(position);

        holder.jobTitle.setText(entry.getJobTitle());
        if (!entry.getJobPhone().isEmpty()) {
            holder.phoneNumber.setText(entry.getJobPhone());
            Linkify.addLinks(holder.phoneNumber, Linkify.PHONE_NUMBERS);
        } else {
            holder.phoneNumber.setVisibility(View.GONE);
        }
            AddressFormat addressFormat = new AddressFormat(
                    entry.getJobStreet1(),
                    entry.getJobStreet2(),
                    entry.getJobCity(),
                    entry.getJobState(),
                    entry.getJobZipCode()
            );
            holder.address.setText(addressFormat.addressFormat());

            if (!"No Address Entered".contentEquals(holder.address.getText())) {
                holder.address.setPaintFlags(holder.address.getPaintFlags()
                        | Paint.UNDERLINE_TEXT_FLAG);
                holder.address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri gmmIntentUri =
                                Uri.parse("geo:0,0?q=" + holder.address.getText().toString());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(frag.getPackageManager()) != null) {
                            frag.startActivity(mapIntent);
                        }

                    }
                });
            }


        holder.jobEntries.setText("Job Entries\n" + entry.getJobEntries().intValue());
        holder.expenseEntries.setText("Expenses\n" + entry.getExpenseEntries().intValue());

        // Testing Save state when navigating away using navigation drawer
        holder.clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClockIn(true, entry.getGeneratedJobId(), entry.getJobTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView jobTitle;
        AppCompatTextView phoneNumber;
        AppCompatTextView address;
        AppCompatTextView jobEntries;
        AppCompatTextView expenseEntries;
        MaterialButton clockIn;
        MaterialCardView parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            address = itemView.findViewById(R.id.address);
            jobEntries = itemView.findViewById(R.id.job_entries);
            expenseEntries = itemView.findViewById(R.id.expense_entries);
            clockIn = itemView.findViewById(R.id.clock_in);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public interface ClockInListener {
        void onClockIn(boolean clockedIn, String job, String jobTitle);
    }
}
