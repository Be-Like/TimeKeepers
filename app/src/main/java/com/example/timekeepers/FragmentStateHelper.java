package com.example.timekeepers;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

public class FragmentStateHelper {

    private FragmentManager fragmentManager;

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Fragment.SavedState> fragmentSavedStates =
            new HashMap<>();

    public FragmentStateHelper(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void restoreState(Fragment fragment, Integer key) {
        Fragment.SavedState savedState = fragmentSavedStates.get(key);
        if (!fragment.isAdded()) {
            fragment.setInitialSavedState(savedState);
        }
    }

    public void saveState(Fragment fragment, Integer key) {
        if (fragment != null && fragment.isAdded()) {
            fragmentSavedStates.put(key, fragmentManager.saveFragmentInstanceState(fragment));
        }
    }

    public Bundle saveHelperState() {
        Bundle state = new Bundle();

        for(Map.Entry<Integer, Fragment.SavedState> entry : fragmentSavedStates.entrySet()) {
            state.putParcelable(entry.getKey().toString(), entry.getValue());
        }

        return state;
    }

    public void restoreHelperState(Bundle savedState) {
        fragmentSavedStates.clear();
        savedState.setClassLoader(ClassLoader.getSystemClassLoader());
        for (String key : savedState.keySet()) {
            Fragment.SavedState state = savedState.getParcelable(key);
            fragmentSavedStates.put(Integer.valueOf(key), state);
        }
    }
}
