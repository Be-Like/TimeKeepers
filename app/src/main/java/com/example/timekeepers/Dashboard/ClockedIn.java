package com.example.timekeepers.Dashboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timekeepers.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClockedIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClockedIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClockedIn extends Fragment {
    private static final String BUNDLE_KEY = "ClockedInBundle";
    private Bundle jobInformation;

    private View fragmentView;
    private AppCompatTextView jobTitle;

    private OnFragmentInteractionListener mListener;

    public ClockedIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bundle Parameter 1.
     * @return A new instance of fragment ClockedIn.
     */
    public static ClockedIn newInstance(Bundle bundle) {
        ClockedIn fragment = new ClockedIn();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobInformation = getArguments().getBundle(BUNDLE_KEY);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_clocked_in, container, false);

        initViews();
        setViews();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void initViews() {
        jobTitle = fragmentView.findViewById(R.id.job_title);
    }
    private void setViews() {
        jobTitle.setText(jobInformation.getString(getString(R.string.jobTitleKey)));
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
}
