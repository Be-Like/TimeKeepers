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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;


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
                                        // TODO: Continue here once the add expense functionality is completed
//                                        expenseEntry.put(doc.getId(), new ExpenseEntryObject(
//                                                jobId,
//                                                doc.getId(),
//                                        ));
                                    }
                                }
                            });
                }
            }
        });
    }
}
