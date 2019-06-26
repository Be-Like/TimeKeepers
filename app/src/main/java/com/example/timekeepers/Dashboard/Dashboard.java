package com.example.timekeepers.Dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
public class Dashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String dashboardTitle = "Dashboard";

    private View fragmentView;

    private ArrayList<JobObject> jobsArray;

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

        initRecyclerView();


        // Inflate the layout for this fragment
        return fragmentView;
    }

    public void onStart() {
        super.onStart();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                            RecyclerView recyclerView =
                                    fragmentView.findViewById(R.id.recycler_view);
                            DashboardAdapter adapter =
                                    new DashboardAdapter(getContext(), jobsArray, getActivity());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });
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
