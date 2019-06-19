package com.example.timekeepers.JobManagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeepers.Misc.CurrencyListFormatter;
import com.example.timekeepers.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ViewHolder> {
    private ArrayList<JobObject> jobs;
    private Context context;

    ManagementAdapter(Context context, ArrayList<JobObject> jobs) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position){
        final JobObject entry = jobs.get(position);

        // Format for Money to only show two decimal places
        DecimalFormat df = new DecimalFormat("0");

        holder.jobTitle.setText(
                entry.getJobTitle());
        holder.timeWorked.setText(
                df.format(entry.getHoursWorked()) + " Hrs"
        );
        holder.payRate.setText(
                "$" + CurrencyListFormatter.format(entry.getPayRate())
        );

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idKey = context.getString(R.string.idKey);
                Log.d(TAG, "onClick: adapter class " + idKey);

                Bundle bundle = new Bundle();
                bundle.putString(context.getString(R.string.idKey), entry.getGeneratedJobId());
                bundle.putString(context.getString(R.string.jobTitleKey) , entry.getJobTitle());
                bundle.putString(context.getString(R.string.jobTypeKey) , entry.getJobType());
                bundle.putDouble(context.getString(R.string.payRateKey), entry.getPayRate());
                bundle.putDouble(context.getString(R.string.hoursWorkedKey), entry.getHoursWorked());
                bundle.putBoolean(context.getString(R.string.completedJobKey), entry.getCompletedJob());
                bundle.putString(context.getString(R.string.jobEmailKey), entry.getJobEmail());
                bundle.putDouble(context.getString(R.string.federalTaxKey), entry.getJobFederal());
                bundle.putDouble(context.getString(R.string.grossPayKey), entry.getGrossPay());
                bundle.putDouble(context.getString(R.string.medicareKey), entry.getMedicare());
                bundle.putDouble(context.getString(R.string.socialSecurityKey), entry.getSocialSecurity());
                bundle.putDouble(context.getString(R.string.otherWithholdingsKey), entry.getOtherWithholding());
                bundle.putString(context.getString(R.string.jobPhoneKey), entry.getJobPhone());
                bundle.putDouble(context.getString(R.string.retirementKey), entry.getRetirement());
                bundle.putDouble(context.getString(R.string.stateTaxKey), entry.getStateTax());
                bundle.putString(context.getString(R.string.jobWebsiteKey), entry.getJobWebsite());
                bundle.putString(context.getString(R.string.street1Key), entry.getJobStreet1());
                bundle.putString(context.getString(R.string.street2Key), entry.getJobStreet2());
                bundle.putString(context.getString(R.string.cityKey), entry.getJobCity());
                bundle.putString(context.getString(R.string.stateKey), entry.getJobState());
                bundle.putString(context.getString(R.string.zipCodeKey), entry.getJobZipCode());

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
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView jobTitle;
        TextView timeWorked;
        TextView payRate;
        ConstraintLayout parentLayout;

        // Associating the correct text with the correct layout
        ViewHolder(View itemView){
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            timeWorked = itemView.findViewById(R.id.time_worked);
            payRate = itemView.findViewById(R.id.pay_rate);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
