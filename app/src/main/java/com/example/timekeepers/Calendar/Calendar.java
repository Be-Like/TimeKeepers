package com.example.timekeepers.Calendar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timekeepers.JobEntryObject;
import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Calendar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Calendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calendar extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View fragmentView;

    private HashMap<String, JobEntryObject> calendarEntry;

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
        // TODO: add logic for switching views
        int id = item.getItemId();
        if (id == R.id.calendar_view) {
            Toast.makeText(getContext(), "Will change views.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getEmail();

        if (userEmail == null) {
            Toast.makeText(getContext(), "Error getting user information.",
                    Toast.LENGTH_LONG).show();
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
                                                doc.getDate("Start_Time")
                                        ));
                                    }
                                }
                            });
                }
                // TODO: Default = create a list of all values in the hashmap so I can sort by date.
                //  Reason for not creating a list right away is because even to filter a list you
                //  would need to create a new list. Filtering a hashmap costs less than filtering
                //  an arraylist.
            }
        });
    }
    class SortByDate implements Comparator<JobEntryObject> {
        public int compare(JobEntryObject a, JobEntryObject b) {
            return (a.getStartTime().before(b.getStartTime()) ? 1 : -1);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
