package com.parkingapp.hulapark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User.NewParkingLogDataModel;
import com.parkingapp.hulapark.Utilities.DataBase.DBManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Extras.ExtrasManager;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingHoursSpan;
import com.parkingapp.hulapark.databinding.ActivityFinishParkingScreenBinding;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class FinishParkingScreen extends AppCompatActivity
{
    private ActivityFinishParkingScreenBinding binding;
    private String plateNumber = "";
    private String parkingSpot = "";
    private String parkingDuration = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinishParkingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_finish_parking_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        View customView = getSupportActionBar().getCustomView();

        TextView titleTextView = customView.findViewById(R.id.tvActivityTitle);
        titleTextView.setText(R.string.finishparking_activity_title);

        // UI elements
        User user = (User)CommonFragUtils.FragmentSwapper.getUser();

        ExtrasManager.getPassedExtras(savedInstanceState, getIntent(), (e) ->
        {
            plateNumber = e.getString("INIT_PARKING_PLATE_NUMBER");
            parkingSpot = e.getString("INIT_PARKING_PARKING_SPOT");
            parkingDuration = e.getString("INIT_PARKING_PARKING_DURATION");
        });

        binding.finishPlateNumber.setText(plateNumber);
        binding.finishParkingId.setText(parkingSpot);
        binding.finishParikingDuration.setText(parkingDuration);

        user.getBPHPrice().observe(this, bphPrice -> {
            Double multiplier = Double.parseDouble(parkingDuration) / 60.0f;
            binding.finishPaymentPrice.setText(String.format("%.2f", bphPrice * multiplier));
        });

        binding.commitPaymentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            int minutes = ParkingHoursSpan.fromString(parkingDuration).getMinutes(CommonFragUtils.FragmentSwapper.hourScale);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime then = LocalDateTime.now().plusMinutes(minutes);

            ZoneId zoneId = ZoneId.systemDefault();
            long startTime = now.atZone(zoneId).toInstant().toEpochMilli();
            long finishTime = then.atZone(zoneId).toInstant().toEpochMilli();

            ParkingCardDataModel cardModel = new ParkingCardDataModel(now, then);
            cardModel.setPlateNumber(plateNumber)
                     .setLocationID(parkingSpot)
                     .setPrice(binding.finishPaymentPrice.getText().toString());
            CommonFragUtils.FragmentSwapper.getParkingCardAdapter().pushCard(cardModel);

            NewParkingLogDataModel dataModel = new NewParkingLogDataModel();
            dataModel.FinishTime = finishTime;
            dataModel.Price = Double.parseDouble(binding.finishPaymentPrice.getText().toString());
            dataModel.SectorID = parkingSpot;
            dataModel.VehicleID = plateNumber;

            DBManager.setNewParking(user, startTime, dataModel);

            startActivity(intent);
            FinishParkingScreen.this.finish();
        });
        binding.cancelPaymentBtn.setOnClickListener(view -> {
            FinishParkingScreen.this.finish();
        });
    }
}