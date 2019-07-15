package com.example.timekeepers.Dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String KEY_CLOCKED_IN = "keyClockedIn";
    private static final String KEY_CLOCKED_IN_JOB = "keyClockedInJob";
    Bundle navigationSaveState;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String dashboardTitle = "Dashboard";

    private View fragmentView;
    private RecyclerView recyclerView;
    private ConstraintLayout clockedInView;
    private MaterialButton clockOutButton;

    private ArrayList<JobObject> jobsArray;
    private boolean clockedIn;
    private String clockedInJob;

    private OnFragmentInteractionListener mListener;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance() {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (clockedIn && clockedInJob != null) {
            Log.d(TAG, "onSaveInstanceState: saving clockedIn true status");
            outState.putBoolean(KEY_CLOCKED_IN, clockedIn);
            outState.putString(KEY_CLOCKED_IN_JOB, clockedInJob);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * TODO: Cards will be used to display all the active jobs.
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




        // Set Toolbar Title
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(dashboardTitle);

        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initViews();
        initRecyclerView();

        if (navigationSaveState != null) {
            savedInstanceState = navigationSaveState;
        }
        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: getting clockedIn true status");
            clockedIn = savedInstanceState.getBoolean(KEY_CLOCKED_IN);
            clockedInJob = savedInstanceState.getString(KEY_CLOCKED_IN_JOB);
        } else {
            clockedIn = false;
        }

        // Inflate the layout for this fragment
        return fragmentView;
    }

    public void onStart() {
        super.onStart();

        // TODO: create logic for determining if a job is clocked in or not
        //  and display the corresponding view correctly.
        setClockedInStatus(clockedIn, clockedInJob);
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
        saveCurrentState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG, "onDetach: dashboard detached");
    }

    private void saveCurrentState() {
        if (clockedIn && clockedInJob != null) {
            Log.d(TAG, "saveCurrentState: onTransition to new fragment " +
                    "saving clockedIn true status");
            Bundle savedInstanceState = new Bundle();
            savedInstanceState.putBoolean(KEY_CLOCKED_IN, clockedIn);
            savedInstanceState.putString(KEY_CLOCKED_IN_JOB, clockedInJob);
        }
    }

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

    private void setClockedInStatus(boolean isClockedIn, String job) {
        clockedIn = isClockedIn;
        clockedInJob = job;

        Log.d(TAG, "setClockedInStatus: " + clockedIn + "..." + clockedInJob);

        if (clockedIn && clockedInJob != null) {
            recyclerView.setVisibility(View.GONE);
            clockedInView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            clockedInView.setVisibility(View.GONE);
        }
    }

    public void onClick(View v) {
        if (v == clockOutButton) {
            onClockIn(false, null);
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

    public void onClockIn(boolean clockedIn, String job) {
        setClockedInStatus(clockedIn, job);
    }
}
