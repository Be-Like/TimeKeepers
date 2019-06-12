package com.example.timekeepers.JobManagement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeepers.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ViewHolder> {
    private ArrayList<JobObject> jobs;
    private Context context;

    public ManagementAdapter(Context context, ArrayList<JobObject> jobs) {
        this.jobs = jobs;
        this.context = context;
    }

    // Creation of the view holder for the individual jobs info in the recycler view list
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_job_management_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position){
        final JobObject entry = jobs.get(position);

        // Format for Money to only show two decimal places
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        DecimalFormat df = new DecimalFormat("0.00");

        holder.jobTitle.setText(
                entry.jobTitle);
        holder.timeWorked.setText(
                df.format(entry.hoursWorked)
        );
        holder.payRate.setText(
                currency.format(entry.payRate)
        );

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("jobId", entry.generatedJobId);
                bundle.putString("jobTitle" , entry.jobTitle);
                bundle.putString("jobType" , entry.jobType);
                bundle.putDouble("payRate", entry.payRate);
                bundle.putDouble("hoursWorked", entry.hoursWorked);
                bundle.putBoolean("completedJob", entry.completedJob);
                bundle.putString("jobEmail", entry.jobEmail);
                bundle.putDouble("jobFederal", entry.jobFederal);
                bundle.putDouble("grossPay", entry.grossPay);
                bundle.putDouble("medicare", entry.medicare);
                bundle.putDouble("socialSecurity", entry.socialSecurity);
                bundle.putDouble("otherWithholding", entry.otherWithholding);
                bundle.putString("jobPhone", entry.jobPhone);
                bundle.putDouble("retirement", entry.retirement);
                bundle.putDouble("stateTax", entry.stateTax);
                bundle.putString("jobWebsite", entry.jobWebsite);
                bundle.putString("jobStreet1", entry.jobStreet1);
                bundle.putString("jobStreet2", entry.jobStreet2);
                bundle.putString("jobCity", entry.jobCity);
                bundle.putString("jobState", entry.jobState);
                bundle.putString("jobZipCode", entry.jobZipCode);

                Toast.makeText(context, "Will open: " + entry.getJobTitle(),
                        Toast.LENGTH_SHORT).show();


                Fragment viewJob = JobInformation.newInstance(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, viewJob)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    // Check for job entries count in the recycler view list
    @Override
    public int getItemCount() {
        return jobs.size();
    }

    // View holder with correct data fields for the list
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView jobTitle;
        TextView timeWorked;
        TextView payRate;
        ConstraintLayout parentLayout;

        // Associating the correct text with the correct layout
        public ViewHolder(View itemView){
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            timeWorked = itemView.findViewById(R.id.time_worked);
            payRate = itemView.findViewById(R.id.pay_rate);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
