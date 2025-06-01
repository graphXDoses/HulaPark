package com.parkingapp.hulapark.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Extras.ExtrasManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.UserType;
import com.parkingapp.hulapark.Views.TopNavMenuHolderView;
import com.parkingapp.hulapark.databinding.ActivityAuthScreenBinding;

import java.util.HashMap;

public class BalanceIncScreen extends AppCompatActivity
{
    private Double incAmmount;

    @Override
    public void onBackPressed()
    {
        cancelPayment();
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

        balanceIncBackBtn.setOnClickListener(__ -> cancelPayment());

        ExtrasManager.getPassedExtras(savedInstanceState, getIntent(), (e) ->
        {
            incAmmount = e.getDouble("INC_AMOUNT");
            balanceIncAmount.setText(String.format("%.2f", incAmmount));
        });

        balanceIncPayButton.setOnClickListener(__ ->
        {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("shouldInc", true);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }

    private void cancelPayment()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("shouldInc", false);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}