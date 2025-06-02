package com.parkingapp.hulapark.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Extras.ExtrasManager;

import java.util.Objects;

public class BalanceIncScreen extends AppCompatActivity
{
    private Double incAmmount;

    @Override
    public void onBackPressed()
    {
        acceptPayment(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balanceinc_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        View customView = getSupportActionBar().getCustomView();

        TextView titleTextView = customView.findViewById(R.id.tvActivityTitle);
        titleTextView.setText("Ανανεώση Υπολοίπου");

        // UI
        TextView balanceIncAmount = findViewById(R.id.balanceIncAmount);
        MaterialButton balanceIncPayButton = findViewById(R.id.balanceIncPayButton);
        FloatingActionButton balanceIncBackBtn = findViewById(R.id.balanceIncBackBtn);

        TextInputEditText cardOwnerEditText = findViewById(R.id.cardOwnerEditText);
        TextInputEditText cardNumberEditText = findViewById(R.id.cardNumberEditText);
        TextInputEditText expiryYearDateEditText = findViewById(R.id.expiryYearDateEditText);
        TextInputEditText expiryMonthDateEditText = findViewById(R.id.expiryMonthDateEditText);
        TextInputEditText cvcEditText = findViewById(R.id.cvcEditText);

        balanceIncBackBtn.setOnClickListener(__ -> acceptPayment(false));

        ExtrasManager.getPassedExtras(savedInstanceState, getIntent(), (e) ->
        {
            incAmmount = e.getDouble("INC_AMOUNT");
            balanceIncAmount.setText(String.format("%.2f", incAmmount));
        });

        balanceIncPayButton.setOnClickListener(__ ->
        {
            boolean isNoneEmpty = true;

            isNoneEmpty = !Objects.requireNonNull(cardOwnerEditText.getText()).toString().isEmpty();
            isNoneEmpty &= !Objects.requireNonNull(cardNumberEditText.getText()).toString().isEmpty();
            isNoneEmpty &= !Objects.requireNonNull(expiryYearDateEditText.getText()).toString().isEmpty();
            isNoneEmpty &= !Objects.requireNonNull(expiryMonthDateEditText.getText()).toString().isEmpty();
            isNoneEmpty &= !Objects.requireNonNull(cvcEditText.getText().toString()).isEmpty();
            
            if (isNoneEmpty)
                acceptPayment(true);
            else
                Toast.makeText(this, "Συμπληρώστε όλα τα πεδία της κάρτας", Toast.LENGTH_SHORT).show();
        });
    }

    private void acceptPayment(Boolean value)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("shouldInc", value);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}