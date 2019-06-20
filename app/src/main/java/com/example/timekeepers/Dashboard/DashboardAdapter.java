package com.example.timekeepers.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private ArrayList<JobObject> jobs;
    private Context context;
    private FragmentActivity frag;

    DashboardAdapter(Context context, ArrayList<JobObject> jobs, FragmentActivity frag) {
        this.jobs = jobs;
        this.context = context;
        this.frag = frag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_card_item, parent, false);
        return new ViewHolder(view);
    }

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
        if (!"No Address Entered".equals(addressFormat(entry))) {
            holder.address.setText(addressFormat(entry));
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
        } else {
            holder.address.setText(addressFormat(entry));
        }
        holder.clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clock In: " + entry.getJobTitle(),
                        Toast.LENGTH_LONG).show();
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
        MaterialButton clockIn;
        CardView parentLayout;

        ViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            address = itemView.findViewById(R.id.address);
            clockIn = itemView.findViewById(R.id.clock_in);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    private String addressFormat(JobObject jobObject) {
        String passedStreet1 = jobObject.getJobStreet1();
        String passedStreet2 = jobObject.getJobStreet2();
        String passedCity = jobObject.getJobCity();
        String passedState = jobObject.getJobState();
        String passedZipCode = jobObject.getJobZipCode();

        // Assertions
        assert passedStreet1 != null;
        assert passedStreet2 != null;
        assert passedCity != null;
        assert passedState != null;
        assert passedZipCode != null;

        String jobAddress;

        jobAddress = passedStreet1;
        if (!passedStreet2.isEmpty()) {
            if (!jobAddress.isEmpty()) {
                jobAddress = jobAddress + "\n";
            }
            jobAddress = jobAddress + passedStreet2;
        }
        if (!passedCity.isEmpty()) {
            if (!jobAddress.isEmpty()) {
                jobAddress = jobAddress + "\n";
            }
            jobAddress = jobAddress + passedCity;
        }
        if (!passedState.isEmpty()) {
            jobAddress = jobAddress + " " + passedState;
        }
        if (!passedZipCode.isEmpty()) {
            jobAddress = jobAddress + " " + passedZipCode;
        }

        if (jobAddress.isEmpty()) {
            jobAddress = "No Address Entered";
        }

        return jobAddress;
    }
}
