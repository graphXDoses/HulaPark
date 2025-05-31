package com.parkingapp.hulapark.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.InputFieldFormatCheckers.AfterTextWatcher;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingHoursSpan;
import com.parkingapp.hulapark.databinding.ActivityHomeScreenBinding;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminScreen extends AppCompatActivity {

    private TextInputEditText cvvNumber;
    private TextInputEditText cardNumber;

    boolean cvvNumberPatternMatched;
    boolean cardNumberPatternMatched;
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
        titleTextView.setText(R.string.admin_activity_title);


    }

    private void creditCardTests()
    {
        //pattern supports Visa Card, Visa Master Card, Union Pay Card
        Pattern cardNumberPattern = Pattern.compile("^(4[0-9]{12}(?:[0-9]{3})?)|" +
                "((?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}))$");
        Pattern cvvPattern = Pattern.compile("^[0-9]{3,4}$");

        cvvNumber = findViewById(R.id.cvcEditText);
        cardNumber = findViewById(R.id.cardNumberEditText);
        cvvNumberPatternMatched = false;
        cardNumberPatternMatched = false;

        cvvNumber.addTextChangedListener(new AfterTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                if (cvvPattern.matcher(s.toString()).matches())
                    cvvNumberPatternMatched = true;
                else
                    cvvNumberPatternMatched = false;
            }
        });

        cardNumber.addTextChangedListener(new AfterTextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                if (cardNumberPattern.matcher(s.toString()).matches())
                    cardNumberPatternMatched = true;
                else
                    cardNumberPatternMatched = false;
            }
        });

        findViewById(R.id.testBtn).setOnClickListener(view -> {

            boolean matchedFields = cvvNumberPatternMatched && cardNumberPatternMatched;
            if (!matchedFields){
                if(!cvvNumberPatternMatched)
                    cvvNumber.setError("Συμπληρώστε σωστά τον αριθμό του CVV.");
                if(!cardNumberPatternMatched)
                    cardNumber.setError("Μη έγκυρη κάρτα. Συμπληρώστε σωστά τον αριθμό της κάρτας.");
            }else {
                Toast.makeText(this, "Έγκυρα στοιχεία!", Toast.LENGTH_SHORT).show();
            } });
    }
}