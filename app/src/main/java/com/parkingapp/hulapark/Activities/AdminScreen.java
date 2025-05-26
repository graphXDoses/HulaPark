package com.parkingapp.hulapark.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.InputFieldFormatCheckers.AfterTextWatcher;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingHoursSpan;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminScreen extends AppCompatActivity {

    private TextInputEditText cvvNumber;
    private TextInputEditText cardNumber;

    boolean cvvNumberPatternMatched;
    boolean cardNumberPatternMatched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        View customView = getSupportActionBar().getCustomView();

        TextView titleTextView = customView.findViewById(R.id.tvActivityTitle);
        titleTextView.setText(R.string.admin_activity_title);

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