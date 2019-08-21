package com.example.timekeepers.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.timekeepers.JobEntryObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Calendar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Calendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calendar extends Fragment
        implements View.OnClickListener, CalendarView.OnDateChangeListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View fragmentView;
    private CalendarView calendarView;
    private RecyclerView calendarSelectionList;
    private RecyclerView recyclerView;
    private FloatingActionButton addJobEntry;

    private HashMap<String, JobEntryObject> calendarEntry;
    private Date selectedDate;

    public Calendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static Calendar newInstance() {
        Calendar fragment = new Calendar();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        // Set Toolbar Title
        String calendarTitle = "Calendar";
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(calendarTitle);

        fragmentView = inflater.inflate(R.layout.fragment_calendar, container, false);
        setHasOptionsMenu(true);

        initViews();
        initRecyclerView();

        // Inflate the layout for this fragment
        return fragmentView;
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
            } else {
                calendarView.setVisibility(View.VISIBLE);
                calendarSelectionList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
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
        addJobEntry = fragmentView.findViewById(R.id.add_job_entry);
        addJobEntry.setOnClickListener(this);
    }

    private void initRecyclerView() {
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getEmail();

        if (userEmail == null) {
            Toast.makeText(getContext(), "Error getting user information.",
                    Toast.LENGTH_LONG).show();
        }

        assert userEmail != null;
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
            if (a.getStartTime() == b.getStartTime()) {
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
        mListener = null;
    }

    public void onClick(View view) {
        if (view == addJobEntry) {
            // TODO: add the logic for adding job entries manually
            Log.d(TAG, "Calendar Entry HashMap: " + calendarEntry.toString());
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
}
