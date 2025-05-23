package com.parkingapp.hulapark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;

import java.util.Arrays;
import java.util.List;
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

        findViewById(R.id.transitToPaymentBtn).setOnClickListener(view -> {
            Intent intent = new Intent(this, FinishParkingScreen.class);
            intent.putExtra("INIT_PARKING_PLATE_NUMBER", plateNumber.getText().toString());
            intent.putExtra("INIT_PARKING_PARKING_SPOT", parkingSpot.getText().toString());
            intent.putExtra("INIT_PARKING_PARKING_DURATION", parkingDuration.getText().toString());
            startActivity(intent);
        });

        GeoJsonDataModel model = CommonFragUtils.FragmentSwapper.getGeoLocModel();

        List<String> addresses = model.data.features.stream()
                .map(feature -> feature.properties.address)
                .collect(Collectors.toList());

        List<String> hours = Arrays.asList(30, 60, 90, 120, 150, 180 , 210, 240).stream()
                            .map(i ->
                            {
                                String postfix = i % 60 == 30 ? "½" : "";
                                postfix += i / 60 <= 1 ? " ώρα" : " ώρες";
                                return (i / 60 == 0 ? "" : i / 60) + postfix;
                            })
                            .collect(Collectors.toList());

        parkingSpot.setAdapter(new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, addresses));
        parkingDuration.setAdapter(new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hours));
    }
}