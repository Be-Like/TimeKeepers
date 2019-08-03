package com.example.timekeepers.Dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dashboard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment implements DashboardAdapter.ClockInListener,
        View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "PassedClockedInStatus";
    private static final String ARG_PARAM2 = "PassedClockInJobID";
    private static final String ARG_PARAM3 = "PassedClockInJobTitle";

    private String dashboardTitle = "Dashboard";

    private View fragmentView;
    private RecyclerView recyclerView;
    private ConstraintLayout clockedInView;
    private MaterialButton clockOutButton;

    private ArrayList<JobObject> jobsArray;
    private boolean clockedIn;
    private String clockedInJobID;
    private String clockedInJobTitle;

    private OnFragmentInteractionListener mListener;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isClockedIn Parameter 1.
     * @param jobID Parameter 2.
     * @param jobTitle Parameter 3.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(boolean isClockedIn, String jobID, String jobTitle) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isClockedIn);
        args.putString(ARG_PARAM2, jobID);
        args.putString(ARG_PARAM2, jobTitle);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (clockedIn && clockedInJob != null) {
//            Log.d(TAG, "onSaveInstanceState: saving clockedIn true status");
//            outState.putBoolean(KEY_CLOCKED_IN, clockedIn);
//            outState.putString(KEY_CLOCKED_IN_JOB_ID, clockedInJobID);
//            outState.putString(KEY_CLOCKED_IN_JOB_TITLE, clockedInJobTitle);
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clockedIn = getArguments().getBoolean(ARG_PARAM1);
            clockedInJobID = getArguments().getString(ARG_PARAM2);
            clockedInJobTitle = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Functions of the card include:
         *  - Title (Done)
         *  - Address (Done)
         *  - Phone Number (Done)
         *  - Clock-In Button (XML done)
         *  - Add Job Entry Button
         *  - Number of Job Entries (XML Layout done)
         *  - Number of Expense Entries (XML Layout done)
         *
         * Cards fill full screen using a parent child relationship
         *
         * Potentially use swipes to clock in or to add job entries
         */



        // TODO: work on the clocked in view and the background timer service

        // Set Toolbar Title
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(dashboardTitle);

        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initViews();
        initRecyclerView();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    public void onStart() {
        super.onStart();
        setClockedInStatus(clockedIn, clockedInJobID, clockedInJobTitle);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        saveCurrentState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG, "onDetach: dashboard detached");
    }

//    private void saveCurrentState() {
//        if (clockedIn && clockedInJob != null) {
//            Log.d(TAG, "saveCurrentState: onTransition to new fragment " +
//                    "saving clockedIn true status");
//            Bundle savedInstanceState = new Bundle();
//            savedInstanceState.putBoolean(KEY_CLOCKED_IN, clockedIn);
//            savedInstanceState.putString(KEY_CLOCKED_IN_JOB, clockedInJob);
//        }
//    }

    private void initRecyclerView() {
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getEmail();
        Log.d(TAG, "initRecyclerView: Dashboard " + userEmail);

        if (userEmail == null) {
            Toast.makeText(getContext(), "Error getting user information.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(userEmail)
                .collection("Users_Jobs")
                .whereEqualTo("Completed", false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        jobsArray = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            jobsArray.add(new JobObject(
                                    doc.getString("Job_Title"),
                                    doc.getString("Job_Type"),
                                    doc.getDouble("Pay_Rate"),
                                    doc.getDouble("Hours_Worked"),
                                    doc.getBoolean("Completed"),
                                    doc.getString("Email"),
                                    doc.getDouble("Federal_Income_Tax"),
                                    doc.getDouble("Gross_Pay"),
                                    doc.getDouble("Medicare"),
                                    doc.getDouble("OASDI"),
                                    doc.getDouble("Other_Withholdings"),
                                    doc.getString("Phone"),
                                    doc.getDouble("Retirement_Contribution"),
                                    doc.getDouble("State_Income_Tax"),
                                    doc.getString("Website"),
                                    doc.getString("Address.Street_1"),
                                    doc.getString("Address.Street_2"),
                                    doc.getString("Address.City"),
                                    doc.getString("Address.State"),
                                    doc.getString("Address.Zip_Code"),
                                    doc.getId()
                            ));
                            Log.d(TAG, "onSuccess: jobsArray= " + jobsArray);
                            DashboardAdapter adapter =
                                    new DashboardAdapter(getContext(), jobsArray,
                                            getActivity(), Dashboard.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });
    }

    private void initViews() {
        recyclerView =
                fragmentView.findViewById(R.id.recycler_view);
        clockedInView = fragmentView.findViewById(R.id.clocked_in);
        clockOutButton = fragmentView.findViewById(R.id.clock_out);
        clockOutButton.setOnClickListener(this);
    }

    private void setClockInTextValues(String job) {
        AppCompatTextView jobTitleLabel = fragmentView.findViewById(R.id.clock_in_job_title);
        jobTitleLabel.setText(job);
    }

    private void setClockedInStatus(boolean isClockedIn, String jobID, String jobTitle) {
        clockedIn = isClockedIn;
        clockedInJobID = jobID;
        clockedInJobTitle = jobTitle;

        Log.d(TAG, "setClockedInStatus: " + clockedIn + "..." + clockedInJobID + "..." + clockedInJobTitle);

        if (clockedIn && clockedInJobID != null && clockedInJobTitle != null) {
            setClockInTextValues(jobTitle);
            recyclerView.setVisibility(View.GONE);
            clockedInView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            clockedInView.setVisibility(View.GONE);
        }

        // Set the clocked in status in the MainActivity
        ((MainActivity) getActivity()).setClockedInStatus(isClockedIn);
        ((MainActivity) getActivity()).setClockedInJobTitle(jobTitle);
        ((MainActivity) getActivity()).setClockedInJobID(jobID);
        // TODO: figure out why it is no longer saving the clock in status...
    }

    public void onClick(View v) {
        if (v == clockOutButton) {
            onClockIn(false, null, null);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onClockIn(boolean clockedIn, String job, String jobTitle) {
        setClockedInStatus(clockedIn, job, jobTitle);
    }
}
