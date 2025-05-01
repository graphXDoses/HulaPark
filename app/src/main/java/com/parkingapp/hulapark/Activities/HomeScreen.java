package com.parkingapp.hulapark.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.parkingapp.hulapark.databinding.ActivityHomeBinding;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}