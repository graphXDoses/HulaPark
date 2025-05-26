package com.parkingapp.hulapark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Utilities.InputFieldFormatCheckers.AfterTextWatcher;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingHoursSpan;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InitParkingScreen extends AppCompatActivity
{
    private TextInputEditText plateNumber;
    private AutoCompleteTextView parkingSpot;
    private AutoCompleteTextView parkingDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_parking_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        View customView = getSupportActionBar().getCustomView();

        TextView titleTextView = customView.findViewById(R.id.tvActivityTitle);
        titleTextView.setText(R.string.initparking_activity_title);

        // UI elements
        plateNumber = ((TextInputEditText)findViewById(R.id.init_plate_number));
        parkingSpot = ((AutoCompleteTextView)findViewById(R.id.init_pariking_spot));
        parkingDuration = ((AutoCompleteTextView)findViewById(R.id.init_pariking_duration));

        plateNumber.setFilters(new InputFilter[]
        {
                new InputFilter.AllCaps()
        });

        Pattern plateNumberTypingPattern = Pattern.compile("^[A-Z]{3}[0-9]{0,4}|[A-Z]{0,3}$");

        plateNumber.addTextChangedListener(new AfterTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                if (!plateNumberTypingPattern.matcher(s.toString()).matches())
                    plateNumber.setError("Εσφαλμένη μορφή πινακίδας. Πρέπει να είναι 3 κεφαλαίοι χαρακτήρες και 4 αριθμοί (πχ. NAB1234)");
                else
                    plateNumber.setError(null);
            }
        });

        findViewById(R.id.transitToPaymentBtn).setOnClickListener(view ->
        {
            Intent intent = new Intent(this, FinishParkingScreen.class);

            Feature sectorFeature = CommonFragUtils.FragmentSwapper.getGeoLocModel()
                                    .data.features.stream().filter(feature -> feature.properties.address.equals(parkingSpot.getText().toString()))
                                    .collect(Collectors.toList()).get(0);

            String parkingDurationString = String.valueOf(ParkingHoursSpan.fromString(parkingDuration.getText().toString()).getMinutes());

            intent.putExtra("INIT_PARKING_PLATE_NUMBER", plateNumber.getText().toString());
            intent.putExtra("INIT_PARKING_PARKING_SPOT", sectorFeature.properties.sectorID);
            intent.putExtra("INIT_PARKING_PARKING_DURATION", parkingDurationString);
            startActivity(intent);
        });

        GeoJsonDataModel model = CommonFragUtils.FragmentSwapper.getGeoLocModel();

        List<String> addresses = model.data.features.stream()
                .map(feature -> feature.properties.address)
                .collect(Collectors.toList());

        List<String> hours = Arrays.stream(ParkingHoursSpan.values())
                                   .map(h -> h.toString())
                                   .collect(Collectors.toList());

        parkingSpot.setAdapter(new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, addresses));
        parkingDuration.setAdapter(new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hours));
    }
}