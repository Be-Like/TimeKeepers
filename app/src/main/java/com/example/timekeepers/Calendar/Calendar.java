package com.example.timekeepers.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.timekeepers.JobEntryObject;
import com.example.timekeepers.JobManagement.JobObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calendar extends Fragment
        implements View.OnClickListener, CalendarView.OnDateChangeListener {

    private View fragmentView;
    private CalendarView calendarView;
    private RecyclerView calendarSelectionList;
    private RecyclerView recyclerView;
    private FloatingActionButton addJobEntryButton;

    private String userEmail;

    private HashMap<String, JobEntryObject> calendarEntry;
    private Date selectedDate;

    private SharedPreferences sharedPreferences;
    private boolean isCalendarView;
    private final String CALENDAR_VIEW_STATE = "calendar_view_state";

    public Calendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Calendar.
     */
    public static Calendar newInstance() {
        return new Calendar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = Objects.requireNonNull(getActivity()).getApplicationContext()
                .getSharedPreferences("com.example.timeKeepers.calendar", Context.MODE_PRIVATE);
        setIsCalendarView(sharedPreferences.getBoolean(CALENDAR_VIEW_STATE, true));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set Toolbar Title
        final String calendarTitle = "Calendar";
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(calendarTitle);
        ((MainActivity) getActivity()).lockNavigationDrawer(false);

        fragmentView = inflater.inflate(R.layout.fragment_calendar, container, false);
        setHasOptionsMenu(true);

        initViews();
        if (!getIsCalendarView()) {
            calendarView.setVisibility(View.GONE);
            calendarSelectionList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        initRecyclerView();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_view, menu);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calendar_view) {
            if (calendarView.getVisibility() == View.VISIBLE) {
                calendarView.setVisibility(View.GONE);
                calendarSelectionList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                setIsCalendarView(false);
            } else {
                calendarView.setVisibility(View.VISIBLE);
                calendarSelectionList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                setIsCalendarView(true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSelectedDayChange(CalendarView v, int year, int month, int day) {
        selectedDate = new GregorianCalendar(year, month, day).getTime();
        createDateRecycler(getJobEntriesForDate(selectedDate));
    }

    private void initViews() {
        calendarView = fragmentView.findViewById(R.id.calendar_calendar);
        calendarView.setOnDateChangeListener(this);
        calendarSelectionList = fragmentView.findViewById(R.id.calendar_selection_list);
        recyclerView = fragmentView.findViewById(R.id.calendar_list);
        selectedDate = java.util.Calendar.getInstance().getTime();
        addJobEntryButton = fragmentView.findViewById(R.id.add_job_entry);
        addJobEntryButton.setOnClickListener(this);
    }

    private void initRecyclerView() {
        userEmail = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getEmail();

        if (userEmail == null) {
            Toast.makeText(getContext(), "Error getting user information.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        final CollectionReference usersJobs = FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(userEmail)
                .collection("Users_Jobs");

        usersJobs.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot value) {
                calendarEntry = new HashMap<>();
                for (QueryDocumentSnapshot document : value) {
                    final String jobId = document.getId();

                    usersJobs.document(document.getId())
                            .collection("Job_Entries")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                    @Nullable FirebaseFirestoreException e) {
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        calendarEntry.put(doc.getId(), new JobEntryObject(
                                                jobId,
                                                doc.getId(),
                                                doc.getDouble("Break_Time"),
                                                doc.getDate("End_Time"),
                                                doc.getDouble("Hours_Worked"),
                                                doc.getString("Job_Title"),
                                                doc.getString("Notes"),
                                                doc.getDouble("Pay"),
                                                doc.getDate("Start_Time"),
                                                doc.getString("Address.Street_1"),
                                                doc.getString("Address.Street_2"),
                                                doc.getString("Address.City"),
                                                doc.getString("Address.State"),
                                                doc.getString("Address.Zip_Code")
                                        ));
                                        ArrayList<JobEntryObject> jobsList = new ArrayList<>(calendarEntry.values());
                                        Collections.sort(jobsList, new SortByDate());
                                        createRecycler(jobsList);
                                        createDateRecycler(getJobEntriesForDate(selectedDate));
                                    }
                                }
                            });
                }
            }
        });
    }
    class SortByDate implements Comparator<JobEntryObject> {
        public int compare(JobEntryObject a, JobEntryObject b) {
            if (a.getStartTime().equals(b.getStartTime())) {
                return 0;
            } else {
                return (a.getStartTime().before(b.getStartTime()) ? 1 : -1);
            }
        }
    }
    private ArrayList<JobEntryObject> getJobEntriesForDate(Date date) {
        ArrayList<JobEntryObject> list = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd-MMM-yyyy");
        String dateString = dateFormat.format(date);
        for (JobEntryObject jobEntry : calendarEntry.values()) {
            if (dateFormat.format(jobEntry.getStartTime()).equals(dateString)
                    || dateFormat.format(jobEntry.getEndTime()).equals(dateString)) {
                list.add(jobEntry);
            }
        }
        Collections.sort(list, new SortByDate());
        return list;
    }

    private void createRecycler(ArrayList<JobEntryObject> list) {
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(calendarAdapter);
    }
    private void createDateRecycler(ArrayList<JobEntryObject> list) {
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), list);
        calendarSelectionList.setLayoutManager(new LinearLayoutManager(getContext()));
        calendarSelectionList.setAdapter(calendarAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onClick(View view) {
        if (view == addJobEntryButton) {
            addJobEntry();
        }
    }

    private void addJobEntry() {
        // Select Job to add an entry to
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(userEmail)
                .collection("Users_Jobs")
                .whereEqualTo("Completed", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final ArrayList<JobObject> activeJobs = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            activeJobs.add(new JobObject(
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
                        }
                        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                .setTitle("Select Job to Add Entry to:")
                                .setAdapter(createListAdapter(activeJobs),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int i) {
                                                AddJobEntry entry =
                                                        AddJobEntry
                                                                .newInstance(activeJobs.get(i));

                                                FragmentManager fm =
                                                        Objects.requireNonNull(getActivity())
                                                                .getSupportFragmentManager();

                                                fm.beginTransaction()
                                                        .replace(R.id.main_fragment, entry)
                                                        .addToBackStack(null)
                                                        .commit();
                                            }
                                        }).show();
                    }
                });
    }

    private ListAdapter createListAdapter(ArrayList<JobObject> list) {

        ArrayList<String> jobTitles = new ArrayList<>();
        for (JobObject o : list) {
            jobTitles.add(o.getJobTitle());
        }

        return new ArrayAdapter<String>(Objects.requireNonNull(getContext()),
                android.R.layout.select_dialog_item, android.R.id.text1, jobTitles) {
            @NonNull
            public View getView(int pos, View convertView, @NonNull ViewGroup parent) {
                return super.getView(pos, convertView, parent);
            }
        };
    }

    public void setIsCalendarView(Boolean isCalendarView) {
        this.isCalendarView = isCalendarView;
        sharedPreferences.edit().putBoolean(CALENDAR_VIEW_STATE, isCalendarView).apply();
    }
    public boolean getIsCalendarView() {
        return this.isCalendarView;
    }
}
