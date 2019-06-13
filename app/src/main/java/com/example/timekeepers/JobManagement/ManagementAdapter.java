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
                entry.getJobTitle());
        holder.timeWorked.setText(
                df.format(entry.getHoursWorked())
        );
        holder.payRate.setText(
                currency.format(entry.getPayRate())
        );

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: set key values in the strings.xml file.
                Bundle bundle = new Bundle();
                bundle.putString("jobId", entry.getGeneratedJobId());
                bundle.putString("jobTitle" , entry.getJobTitle());
                bundle.putString("jobType" , entry.getJobType());
                bundle.putDouble("payRate", entry.getPayRate());
                bundle.putDouble("hoursWorked", entry.getHoursWorked());
                bundle.putBoolean("completedJob", entry.getCompletedJob());
                bundle.putString("jobEmail", entry.getJobEmail());
                bundle.putDouble("jobFederal", entry.getJobFederal());
                bundle.putDouble("grossPay", entry.getGrossPay());
                bundle.putDouble("medicare", entry.getMedicare());
                bundle.putDouble("socialSecurity", entry.getSocialSecurity());
                bundle.putDouble("otherWithholding", entry.getOtherWithholding());
                bundle.putString("jobPhone", entry.getJobPhone());
                bundle.putDouble("retirement", entry.getRetirement());
                bundle.putDouble("stateTax", entry.getStateTax());
                bundle.putString("jobWebsite", entry.getJobWebsite());
                bundle.putString("jobStreet1", entry.getJobStreet1());
                bundle.putString("jobStreet2", entry.getJobStreet2());
                bundle.putString("jobCity", entry.getJobCity());
                bundle.putString("jobState", entry.getJobState());
                bundle.putString("jobZipCode", entry.getJobZipCode());

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
