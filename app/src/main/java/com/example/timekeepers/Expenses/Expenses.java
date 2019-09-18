package com.example.timekeepers.Expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.timekeepers.Calendar.AddJobEntry;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;


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
        ((MainActivity) getActivity()).lockNavigationDrawer(false);

        fragmentView = inflater.inflate(R.layout.fragment_expenses, container, false);

        initViews();
        initRecyclerView();

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
            addExpenseEntry();
        }
    }

    private void initViews() {
        recyclerView = fragmentView.findViewById(R.id.expense_list);
        addExpenseEntryButton = fragmentView.findViewById(R.id.add_expense_entry);
        addExpenseEntryButton.setOnClickListener(this);
    }

    private String userEmail;
    private HashMap<String, ExpenseEntryObject> expenseEntry;
    private void initRecyclerView() {
        userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

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
                expenseEntry = new HashMap<>();
                for (QueryDocumentSnapshot document : value) {
                    final String jobId = document.getId();

                    usersJobs.document(document.getId())
                            .collection("Expense_Entries")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                    @Nullable FirebaseFirestoreException e) {
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        expenseEntry.put(doc.getId(), new ExpenseEntryObject(
                                                jobId,
                                                doc.getId(),
                                                doc.getString("Job_Title"),
                                                doc.getString("Vendor"),
                                                doc.getString("Category"),
                                                doc.getDate("Date"),
                                                doc.getDouble("Total_Cost"),
                                                doc.getString("Address.Street_1"),
                                                doc.getString("Address.Street_2"),
                                                doc.getString("Address.City"),
                                                doc.getString("Address.State"),
                                                doc.getString("Address.Zip_Code")
                                        ));
                                        ArrayList<ExpenseEntryObject> expensesList =
                                                new ArrayList<>(expenseEntry.values());
                                        Collections.sort(expensesList, new SortByDate());
                                        createRecycler(expensesList);
                                    }
                                }
                            });
                }
            }
        });
    }

    class SortByDate implements Comparator<ExpenseEntryObject> {
        public int compare(ExpenseEntryObject a, ExpenseEntryObject b) {
            if (a.getExpenseDate().equals(b.getExpenseDate())) {
                return 0;
            } else {
                return (a.getExpenseDate().before(b.getExpenseDate()) ? 1 : -1);
            }
        }
    }
    private void createRecycler(ArrayList<ExpenseEntryObject> list) {
        ExpensesAdapter expensesAdapter = new ExpensesAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expensesAdapter);
    }

    private void addExpenseEntry() {
        // Select Job to add an entry to
        FirebaseFirestore.getInstance()
                .collection("Jobs")
                .document(userEmail)
                .collection("Users_Jobs")
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
                        // TODO:
                        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                .setTitle("Select Job to Add Entry to:")
                                .setAdapter(createListAdapter(activeJobs),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface,
                                                                int i) {
                                                AddExpenseEntry entry =
                                                        AddExpenseEntry
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
}
