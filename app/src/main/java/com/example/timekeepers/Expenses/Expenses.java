package com.example.timekeepers.Expenses;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timekeepers.MainActivity;
import com.example.timekeepers.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Expenses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Expenses extends Fragment implements View.OnClickListener {

    private View fragmentView;

    // View declarations
    private RecyclerView recyclerView;
    private FloatingActionButton addExpenseEntryButton;

    public Expenses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Expenses.
     */
    public static Expenses newInstance() {
        return new Expenses();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set Toolbar Title
        String expensesTitle = "Expenses";
        ((MainActivity) Objects.requireNonNull(getActivity())).toolbar.setTitle(expensesTitle);

        fragmentView = inflater.inflate(R.layout.fragment_expenses, container, false);

        initViews();

        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onClick (View view) {
        if (view == addExpenseEntryButton) {
            // TODO: addExpenseEntryButton
            Toast.makeText(getContext(), "Will add expense.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        recyclerView = fragmentView.findViewById(R.id.expense_list);
        addExpenseEntryButton = fragmentView.findViewById(R.id.add_expense_entry);
        addExpenseEntryButton.setOnClickListener(this);
    }
}
