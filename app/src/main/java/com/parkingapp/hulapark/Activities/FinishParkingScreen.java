package com.parkingapp.hulapark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.parkingapp.hulapark.R;

public class FinishParkingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_parking_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        View customView = getSupportActionBar().getCustomView();

        TextView titleTextView = customView.findViewById(R.id.tvActivityTitle);
        titleTextView.setText(R.string.finishparking_activity_title);

        findViewById(R.id.payBtn).setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            FinishParkingScreen.this.finish();
        });
    }
}