package com.parkingapp.hulapark.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Views.CurvedBottomNavigationView;
import com.parkingapp.hulapark.databinding.ActivityHomeBinding;

public class HomeScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CurvedBottomNavigationView mView = binding.BottomNavBar;
        mView.setSelectedItemId(R.id.nav_wallet);
        mView.setOnNavigationItemSelectedListener(HomeScreen.this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_parking_car:
                Navigation.findNavController(findViewById(R.id.activeFrag)).navigate(R.id.parkingFrag);
                break;
            case R.id.nav_map:
                Navigation.findNavController(findViewById(R.id.activeFrag)).navigate(R.id.mapFrag);
                break;
            case R.id.nav_wallet:
                break;
            case R.id.nav_history:
                break;
        }
        return true;
    }
}