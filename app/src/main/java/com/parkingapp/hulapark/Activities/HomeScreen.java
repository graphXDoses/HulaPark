package com.parkingapp.hulapark.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.databinding.ActivityHomeBinding;
import com.simform.custombottomnavigation.Model;

public class HomeScreen extends AppCompatActivity
{
    private ActivityHomeBinding binding;

    private static final int ID_PARKING = 0;
    private static final int ID_MAP = 1;
    private static final int ID_WALLET = 2;
    private static final int ID_HISTORY = 3;
    private static final String KEY_ACTIVE_INDEX = "activeIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int activeIndex = savedInstanceState == null ? ID_PARKING : savedInstanceState.getInt(KEY_ACTIVE_INDEX);

        binding.SSbottomnav.add(new Model(R.drawable.ic_parking_car, ID_PARKING, ID_PARKING, R.string.navstr_parking, com.simform.custombottomnavigation.R.string.empty_value));
        binding.SSbottomnav.add(new Model(R.drawable.ic_map, ID_MAP, ID_MAP, R.string.navstr_map, com.simform.custombottomnavigation.R.string.empty_value));
        binding.SSbottomnav.add(new Model(R.drawable.ic_wallet, ID_WALLET, ID_WALLET, R.string.navstr_wallet, com.simform.custombottomnavigation.R.string.empty_value));
        binding.SSbottomnav.add(new Model(R.drawable.ic_history, ID_HISTORY, ID_HISTORY, R.string.navstr_history, com.simform.custombottomnavigation.R.string.empty_value));

        binding.SSbottomnav.setSelectedIndex(activeIndex);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putInt(KEY_ACTIVE_INDEX, binding.SSbottomnav.getSelectedIndex());
        super.onSaveInstanceState(outState);
    }
}