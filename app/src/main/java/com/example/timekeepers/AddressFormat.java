package com.example.timekeepers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.Objects;

public class AddressFormat {

    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipcode;

    private final String noAddress = "No Address Entered";

    public AddressFormat(String street1, String street2,
                         String city, String state, String zipcode) {
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public String addressFormat() {
        // Assertions
        assert street1 != null;
        assert street2 != null;
        assert city != null;
        assert state != null;
        assert zipcode != null;

        StringBuilder jobAddress = new StringBuilder();

        jobAddress.append(street1);
        if (!street2.isEmpty()) {
            if (!jobAddress.toString().isEmpty()) {
                jobAddress.append("\n");
            }
            jobAddress.append(street2);
        }
        if (!city.isEmpty()) {
            if (!jobAddress.toString().isEmpty()) {
                jobAddress.append("\n");
            }
            jobAddress.append(city);
        }
        if (!state.isEmpty()) {
            jobAddress.append(" " + state);
        }
        if (!zipcode.isEmpty()) {
            jobAddress.append(" " + zipcode);
        }
        if (jobAddress.toString().isEmpty()) {
            jobAddress.append(noAddress);
        }

        return jobAddress.toString();
    }

    public void createMapNavigation(final AppCompatTextView addressView, final Activity activity) {

        if (addressView.getText().toString().equals(noAddress)) {
            return;
        }
        // Underline text
        addressView.setPaintFlags(addressView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Set up navigation to maps for directions or reference
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + addressView.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(Objects.requireNonNull(
                        activity).getPackageManager()) != null) {
                    activity.startActivity(mapIntent);
                }
            }
        });
    }

}
