package com.example.timekeepers.Expenses;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.timekeepers.Misc.CurrencyTextListener;
import com.example.timekeepers.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddEditExpenseParent extends Fragment implements View.OnClickListener {

    private View fragmentView;

    private AppCompatEditText vendorNameView;
    private AppCompatEditText categoryView;
    private AppCompatTextView dateView;
    private TextInputEditText totalCostView;
    private AutoCompleteTextView street1View;
    private AutoCompleteTextView street2View;
    private AutoCompleteTextView cityView;
    private AutoCompleteTextView stateView;
    private AutoCompleteTextView zipcodeView;

    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_expense_entry_form,
                container, false);
        return fragmentView;
    }

    // TODO: Continue with Add and Edit expense functionality -> Expense page
    public void initViews() {
        setVendorNameView((AppCompatEditText) fragmentView.findViewById(R.id.vendor_name));
        setCategoryView((AppCompatEditText) fragmentView.findViewById(R.id.expense_category));

        RelativeLayout dateLayout = fragmentView.findViewById(R.id.date_layout);
        setDateView((AppCompatTextView) fragmentView.findViewById(R.id.expense_date));

        setTotalCostView((TextInputEditText) fragmentView.findViewById(R.id.total_cost));
        CurrencyTextListener currencyTextListener =
                new CurrencyTextListener(getTotalCostView());
        getTotalCostView().addTextChangedListener(currencyTextListener);
        getTotalCostView().setText("0");

//        View addressLayout = fragmentView.findViewById(R.id.address_layout); TODO: This may be needed if there are errors with getting and setting values
        setStreet1View((AutoCompleteTextView) fragmentView.findViewById(R.id.address_first_line));
        setStreet2View((AutoCompleteTextView) fragmentView.findViewById(R.id.address_second_line));
        setCityView((AutoCompleteTextView) fragmentView.findViewById(R.id.city_name));
        setStateView((AutoCompleteTextView) fragmentView.findViewById(R.id.state_name));
        setZipcodeView((AutoCompleteTextView) fragmentView.findViewById(R.id.zipcode));

        dateLayout.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.date_layout) {
            openDatePickerDialog().show();
        }
    }

    private DatePickerDialog openDatePickerDialog() {
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONDAY);
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(Objects.requireNonNull(getContext()),
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
                getDateView().setText(df.format(calendar.getTime()));
                openDatePickerDialog().dismiss();
            }
        }, mYear, mMonth, mDay);
    }

    public boolean isValidVendorInfo() {
        String vendorName = Objects.requireNonNull(getVendorNameView()
                .getText()).toString().trim();
        String category = Objects.requireNonNull(getCategoryView()
                .getText()).toString().trim();
        if (TextUtils.isEmpty(vendorName)) {
            Toast.makeText(getContext(), "Vendor name cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(category)) {
            Toast.makeText(getContext(), "Expense Category cannot be empty",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public boolean isValidDate() {
        if ("".equals(getDateView().getText().toString())) {
            Toast.makeText(getContext(), "Invalid date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Getters and Setters for form fields
    public void setVendorNameView(AppCompatEditText vendorNameView) {
        this.vendorNameView = vendorNameView;
    }
    public AppCompatEditText getVendorNameView() {
        return this.vendorNameView;
    }
    public void setCategoryView(AppCompatEditText categoryView) {
        this.categoryView = categoryView;
    }
    public AppCompatEditText getCategoryView() {
        return this.categoryView;
    }
    public void setDateView(AppCompatTextView dateView) {
        this.dateView = dateView;
    }
    public AppCompatTextView getDateView() {
        return this.dateView;
    }
    public Date getCalendar() {
        return this.calendar.getTime();
    }
    public void setTotalCostView(TextInputEditText totalCostView) {
        this.totalCostView = totalCostView;
    }
    public TextInputEditText getTotalCostView() {
        return this.totalCostView;
    }
    public void setStreet1View(AutoCompleteTextView street1View) {
        this.street1View = street1View;
    }
    public AutoCompleteTextView getStreet1View() {
        return this.street1View;
    }
    public void setStreet2View(AutoCompleteTextView street2View) {
        this.street2View = street2View;
    }
    public AutoCompleteTextView getStreet2View() {
        return this.street2View;
    }
    public void setCityView(AutoCompleteTextView cityView) {
        this.cityView = cityView;
    }
    public AutoCompleteTextView getCityView() {
        return this.cityView;
    }
    public void setStateView(AutoCompleteTextView stateView) {
        this.stateView = stateView;
    }
    public AutoCompleteTextView getStateView() {
        return this.stateView;
    }
    public void setZipcodeView(AutoCompleteTextView zipcodeView) {
        this.zipcodeView = zipcodeView;
    }
    public AutoCompleteTextView getZipcodeView() {
        return this.zipcodeView;
    }
}
