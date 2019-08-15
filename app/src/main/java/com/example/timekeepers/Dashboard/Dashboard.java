package com.example.timekeepers.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String ARG_PARAM4 = "PassedClockInTime";
    private static final String ARG_PARAM5 = "PassedIsOnBreak";
    private static final String ARG_PARAM6 = "PassedBeginBreakTime";
    private static final String ARG_PARAM7 = "PassedEndBreakTime";
    private static final String ARG_PARAM8 = "PassedTotalBreakTime";
    private static final String ARG_PARAM9 = "PassedTimerText";

    private View fragmentView;
    private RecyclerView recyclerView;
    private ConstraintLayout clockedInView;
    private AppCompatTextView beginBreakView;
    private AppCompatTextView endBreakView;
    private MaterialButton clockOutButton;
    private MaterialButton breakButton;
    private AppCompatTextView timerView;

    private ArrayList<JobObject> jobsArray;
    private boolean clockedIn;
    private String clockedInJobID;
    private String clockedInJobTitle;
    private long clockedInTime;
    private boolean isOnBreak;
    private long beginBreakTime;
    private long endBreakTime;
    private long totalBreakTime = 0L;
    private String timerText;

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
     * @param timeStarted Parameter 4.
     * @param isOnBreak Parameter 5.
     * @param beginBreakTime Parameter 6.
     * @param endBreakTime Parameter 7.
     * @param totalBreakTime Parameter 8.
     * @param timerText Parameter 9.
     * @return A new instance of fragment Dashboard.
     */
    public static Dashboard newInstance(boolean isClockedIn, String jobID,
                                        String jobTitle, long timeStarted,
                                        boolean isOnBreak, long beginBreakTime,
                                        long endBreakTime, long totalBreakTime,
                                        String timerText) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isClockedIn);
        args.putString(ARG_PARAM2, jobID);
        args.putString(ARG_PARAM3, jobTitle);
        args.putLong(ARG_PARAM4, timeStarted);
        args.putBoolean(ARG_PARAM5, isOnBreak);
        args.putLong(ARG_PARAM6, beginBreakTime);
        args.putLong(ARG_PARAM7, endBreakTime);
        args.putLong(ARG_PARAM8, totalBreakTime);
        args.putString(ARG_PARAM9, timerText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setClockedIn(getArguments().getBoolean(ARG_PARAM1));
            setClockedInJobID(getArguments().getString(ARG_PARAM2));
            setClockedInJobTitle(getArguments().getString(ARG_PARAM3));
            setClockedInTime(getArguments().getLong(ARG_PARAM4));
            setIsOnBreak(getArguments().getBoolean(ARG_PARAM5));
            setBeginBreakTime(getArguments().getLong(ARG_PARAM6));
            setEndBreakTime(getArguments().getLong(ARG_PARAM7));
            setTotalBreakTime(getArguments().getLong(ARG_PARAM8));
            timerText = getArguments().getString(ARG_PARAM9);
        }
    }

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
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set Toolbar Title
        String dashboardTitle = "Dashboard";
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(dashboardTitle);

        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initViews();
        initRecyclerView();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    public void onStart() {
        super.onStart();
        setClockedInStatus(getClockedIn(), getClockedInJobID(), getClockedInJobTitle());
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
        timerHandler.removeCallbacks(timerRunnable);
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .setTimerText(timerView.getText().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG, "onDetach: dashboard detached");
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
                                    doc.getDouble("Quantity_Job_Entries"),
                                    doc.getDouble("Quantity_Expense_Entries"),
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
        beginBreakView = fragmentView.findViewById(R.id.start_break_time);
        endBreakView = fragmentView.findViewById(R.id.end_break_time);
        clockOutButton = fragmentView.findViewById(R.id.clock_out);
        clockOutButton.setOnClickListener(this);
        breakButton = fragmentView.findViewById(R.id.break_button);
        breakButton.setOnClickListener(this);
        timerView = fragmentView.findViewById(R.id.clock_in_timer);
    }

    private void setClockedInStatus(boolean isClockedIn, String jobID, String jobTitle) {
        setClockedIn(isClockedIn);
        setClockedInJobID(jobID);
        setClockedInJobTitle(jobTitle);

        Log.d(TAG, "setClockedInStatus: " + clockedIn + "..." + clockedInJobID + "..." + clockedInJobTitle);

        if (getClockedIn() && getClockedInJobID() != null && getClockedInJobTitle() != null) {
            if (!getIsOnBreak()) {
                timerHandler.postDelayed(timerRunnable, 0);
                breakButton.setText(getResources().getText(R.string.begin_break));
            } else {
                breakButton.setText(getResources().getText(R.string.end_break));
            }
            setClockInTextValues(jobTitle);
            timerView.setText(timerText);
            setBeginBreakTimeTextValues();
            setEndBreakTimeTextValues();
            recyclerView.setVisibility(View.GONE);
            clockedInView.setVisibility(View.VISIBLE);
        } else {
            timerHandler.removeCallbacks(timerRunnable);
            recyclerView.setVisibility(View.VISIBLE);
            clockedInView.setVisibility(View.GONE);
        }

        // Set the clocked in status in the MainActivity
        ((MainActivity) Objects.requireNonNull(getActivity())).setClockedInStatus(isClockedIn);
        ((MainActivity) getActivity()).setClockedInJobTitle(jobTitle);
        ((MainActivity) getActivity()).setClockedInJobID(jobID);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void setClockInTextValues(String job) {
        AppCompatTextView jobTitleLabel = fragmentView.findViewById(R.id.clock_in_job_title);
        jobTitleLabel.setText(job);
        AppCompatTextView clockedInTime = fragmentView.findViewById(R.id.time_of_clock_in);
        Date date = new Date(getClockedInTime());
        Format dateFormat = new SimpleDateFormat("MMM dd");
        Format timeFormat = new SimpleDateFormat("hh:mm a");
        clockedInTime.setText("Clocked In: " + dateFormat.format(date) + " at " +
                timeFormat.format(date));
    }
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void setBeginBreakTimeTextValues() {
        Date date = new Date(getBeginBreakTime());
        Format dateFormat = new SimpleDateFormat("MMM dd");
        Format timeFormat = new SimpleDateFormat("hh:mm a");
        beginBreakView.setText("Begin Break: " + dateFormat.format(date) + " at " +
                timeFormat.format(date));
        if (getBeginBreakTime() != 0) {
            beginBreakView.setVisibility(View.VISIBLE);
            endBreakView.setVisibility(View.GONE);
        }
    }
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void setEndBreakTimeTextValues() {
        Date date = new Date(getBeginBreakTime());
        Format dateFormat = new SimpleDateFormat("MMM dd");
        Format timeFormat = new SimpleDateFormat("hh:mm a");
        endBreakView.setText("End Break: " + dateFormat.format(date) + " at " +
                timeFormat.format(date));
        if (getEndBreakTime() != 0 && getEndBreakTime() >= getBeginBreakTime()) {
            endBreakView.setVisibility(View.VISIBLE);
        }
    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            Log.d(TAG, "run: " + System.currentTimeMillis() + "..." + getClockedInTime());
            long millis = System.currentTimeMillis() - (getClockedInTime() + getTotalBreakTime());
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int hours = minutes / 60;
            minutes = minutes % 60;

            // set timer text here
            timerView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    private void clearAllBreakData() {
        setIsOnBreak(false);
        setBeginBreakTime(0L);
        setEndBreakTime(0L);
        setTotalBreakTime(0L);
        beginBreakView.setVisibility(View.GONE);
        endBreakView.setVisibility(View.GONE);
        breakButton.setText(getResources().getText(R.string.begin_break));
    }

    private void saveJobEntry() {
        if (getIsOnBreak()) {
            setEndBreakTime(System.currentTimeMillis());
            setTotalBreakTime(calculateTotalBreakTime());
        }
        for (JobObject jobObject : jobsArray) {
            if(jobObject.getGeneratedJobId().equals(getClockedInJobID())) {
                // convert breakTime to double
                long endTime = System.currentTimeMillis();
                double breakTime = (double) getTotalBreakTime() / (60 * 60 * 1000);

                DbWorkEntry workEntry = new DbWorkEntry(jobObject, getClockedInTime(), endTime,
                        breakTime, null);

                workEntry.saveWorkEntryToDb();
                workEntry.updateJobEntryQuantity(1);
                break;
            }
        }
    }

    public void onClick(View v) {
        if (v == clockOutButton) {
            saveJobEntry();
            onClockIn(false, null, null);
            clearAllBreakData();
        }
        if (v == breakButton) {
            if (!getIsOnBreak()) {
                timerHandler.removeCallbacks(timerRunnable);
                setIsOnBreak(true);
                setBeginBreakTime(System.currentTimeMillis());
                setBeginBreakTimeTextValues();
                breakButton.setText(getResources().getText(R.string.end_break));
            } else {
                timerHandler.postDelayed(timerRunnable, 0);
                setIsOnBreak(false);
                setEndBreakTime(System.currentTimeMillis());
                setEndBreakTimeTextValues();
                setTotalBreakTime(calculateTotalBreakTime());
                breakButton.setText(getResources().getText(R.string.begin_break));
            }
        }
    }

    // Getters and Setters for Clocked In Information
    private void setClockedIn(boolean clockedIn) {
        this.clockedIn = clockedIn;
    }
    private boolean getClockedIn() {
        return clockedIn;
    }
    private void setClockedInJobID(String clockedInJobID) {
        this.clockedInJobID = clockedInJobID;
    }
    private String getClockedInJobID() {
        return clockedInJobID;
    }
    private void setClockedInJobTitle(String clockedInJobTitle) {
        this.clockedInJobTitle = clockedInJobTitle;
    }
    private String getClockedInJobTitle() {
        return clockedInJobTitle;
    }
    private void setClockedInTime(long clockedInTime) {
        this.clockedInTime = clockedInTime;
    }
    private long getClockedInTime() {
        return clockedInTime;
    }
    private void setIsOnBreak(boolean isOnBreak) {
        this.isOnBreak = isOnBreak;
        ((MainActivity) Objects.requireNonNull(getActivity())).setIsOnBreak(isOnBreak);
    }
    private boolean getIsOnBreak() {
        return isOnBreak;
    }
    private void setBeginBreakTime(long beginBreakTime) {
        this.beginBreakTime = beginBreakTime;
        ((MainActivity) Objects.requireNonNull(getActivity())).setBeginBreakTime(beginBreakTime);
    }
    private long getBeginBreakTime() {
        return beginBreakTime;
    }
    private void setEndBreakTime(long endBreakTime) {
        this.endBreakTime = endBreakTime;
        ((MainActivity) Objects.requireNonNull(getActivity())).setEndBreakTime(endBreakTime);
    }
    private long getEndBreakTime() {
        return endBreakTime;
    }


    private long calculateTotalBreakTime() {
        return getTotalBreakTime() + (getEndBreakTime() - getBeginBreakTime());
    }
    private void setTotalBreakTime(long totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
        ((MainActivity) Objects.requireNonNull(getActivity())).setTotalBreakTime(totalBreakTime);
    }
    private long getTotalBreakTime() {
        return totalBreakTime;
    }

    private void setMainClockedInTime(long time) {
        ((MainActivity) Objects.requireNonNull(getActivity())).setClockInTime(time);
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
        setClockedInTime(System.currentTimeMillis());
        setMainClockedInTime(System.currentTimeMillis());
    }
}
