package com.example.timekeepers.Misc;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CurrencyTextListener implements TextWatcher {
    private TextInputEditText currencyInput;
    private boolean isDeleting;
    private String pay_rate = "";

    public CurrencyTextListener(TextInputEditText currencyInput) {
        this.currencyInput = currencyInput;
    }

    @Override
    public void onTextChanged(CharSequence s,
                              int start, int before, int count) {}
    public void afterTextChanged(Editable s){
        if (!s.toString().equals(pay_rate)) {
            Log.d(TAG, "afterTextChanged: pay_rate " + s.toString());
            currencyInput.removeTextChangedListener(this);
            String cleanString = s.toString().replaceAll("[^\\d]", "");

            if (isDeleting && s.length() > 0 && !Character.isDigit(s.charAt(s.length() - 1))) {
                cleanString = cleanString.substring(0, cleanString.length() - 1);
            }

            double parsed = 0;
            if (cleanString != null && cleanString.length() > 0) {
                parsed = Double.parseDouble(cleanString);
            }

            String formatted = NumberFormat.getCurrencyInstance().format(parsed/100);
            pay_rate = formatted;
            currencyInput.setText(pay_rate);
            currencyInput.setSelection(formatted.length());
            currencyInput.addTextChangedListener(this);
        }
    }
    public void beforeTextChanged(CharSequence s,
                                  int start, int count, int after){
        if (after <= 0 && count > 0) {
            isDeleting = true;
        }
        else {
            isDeleting = false;
        }
        if (!s.toString().equals(pay_rate)) {
            currencyInput.removeTextChangedListener(this);
            String cleanString = s.toString().replaceAll("[^\\d]", "");
            currencyInput.setText(cleanString);
            currencyInput.addTextChangedListener(this);
        }
    }
}
