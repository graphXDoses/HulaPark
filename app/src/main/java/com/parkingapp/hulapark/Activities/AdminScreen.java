package com.parkingapp.hulapark.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.FloatLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.InputFieldFormatCheckers.AfterTextWatcher;
import com.parkingapp.hulapark.Utilities.Map.HulaMap;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingHoursSpan;
import com.parkingapp.hulapark.databinding.ActivityHomeScreenBinding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminScreen extends AppCompatActivity
{
    private static final int BACK_PRESS_INTERVAL = 2000;
    private long lastBackPressedTime = 0;
    private Toast backToast;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable resetBackPressedFlag;

    @Override
    public void onBackPressed()
    {
        long currentTime = System.currentTimeMillis();
        backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);

        if (currentTime - lastBackPressedTime < BACK_PRESS_INTERVAL) {
            // Avoid stacking
            if (backToast != null) backToast.cancel();
            handler.removeCallbacks(resetBackPressedFlag);
            CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
            finish();
        } else {
            lastBackPressedTime = currentTime;
            if (backToast != null) backToast.cancel();
            backToast.show();

            // Reset the flag after the toast duration
            if (resetBackPressedFlag == null) {
                resetBackPressedFlag = () -> lastBackPressedTime = 0;
            }
            handler.removeCallbacks(resetBackPressedFlag);
            handler.postDelayed(resetBackPressedFlag, BACK_PRESS_INTERVAL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        View customView = getSupportActionBar().getCustomView();

        TextView titleTextView = customView.findViewById(R.id.tvActivityTitle);
        titleTextView.setText("Σελίδα Διαχείρησης");


        HulaMap hulaMap = new HulaMap(findViewById(R.id.adminStreetMap));
        hulaMap
                .setMapFocalPoint(new GeoPoint(21.309884, -157.858140), 13.0)
                .loadMapMarkers()
                .invalidateMap();

        findViewById(R.id.addNewLocation).setOnClickListener(view -> {
            Toast.makeText(this, "New Location added!", Toast.LENGTH_SHORT).show();
        });

        FrameLayout dynamicLayout = findViewById(R.id.displayDynamicFrameLayout);
        LayoutInflater.from(this).inflate(R.layout.inc_display_spot_details_latlon_delete, dynamicLayout, true);
    }
}