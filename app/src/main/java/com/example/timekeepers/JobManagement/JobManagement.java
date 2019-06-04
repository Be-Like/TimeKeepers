package com.example.timekeepers.JobManagement;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;

import java.util.Objects;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobManagement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobManagement extends Fragment
        implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View fragmentView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final String jobManagementTitle = "Job Management";

    private FloatingActionButton addJob;

    private OnFragmentInteractionListener mListener;

    public JobManagement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobManagement.
     */
    // TODO: Rename and change types and number of parameters
    public static JobManagement newInstance(String param1, String param2) {
        JobManagement fragment = new JobManagement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Initialize View
        fragmentView = inflater.inflate(R.layout.fragment_job_management, container, false);

        // Set Toolbar Title
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(jobManagementTitle);
        // TODO: Modify this to be solely in the Add Job fragment... I think.
        ((MainActivity) getActivity()).lockNavigationDrawer(false);

        // Initializers
        initButton();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: is called");
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onClick(View view) {
        if (view == addJob) {
            selectJobTypeDialog();
        }
    }

    private void initButton() {
        addJob = fragmentView.findViewById(R.id.add_job);
        addJob.setOnClickListener(this);
    }

    private void selectJobTypeDialog() {
        // Create the items
        final JobTypeItem[] items = {
                new JobTypeItem("Hourly", R.drawable.ic_hourly),
                new JobTypeItem("Salary", R.drawable.ic_salary),
                new JobTypeItem("Project", R.drawable.ic_project)
        };

        ListAdapter adapter = new ArrayAdapter<JobTypeItem>(getContext(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].jobTypeIcon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (25 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_job_type_prompt)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment addJob = null;

                        switch (which) {
                            case 0:
                                Toast.makeText(getContext(), "Hourly selected", Toast.LENGTH_SHORT).show();
                                addJob = AddJob.newInstance("Hourly");
                                break;
                            case 1:
                                Toast.makeText(getContext(), "Salary selected", Toast.LENGTH_SHORT).show();
                                addJob = AddJob.newInstance("Salary");
                                break;
                            case 2:
                                Toast.makeText(getContext(), "Project selected", Toast.LENGTH_SHORT).show();
                                addJob = AddJob.newInstance("Project");
                                break;
                        }

                        ((MainActivity) getActivity()).showProgress(true);

                        FragmentManager fragmentManager =
                                Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_fragment, addJob)
                                .addToBackStack(null).commit();
                    }
                }).show();
    }
}
