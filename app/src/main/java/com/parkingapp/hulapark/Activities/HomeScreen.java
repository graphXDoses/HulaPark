package com.parkingapp.hulapark.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.window.OnBackInvokedDispatcher;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.Fragments.HistoryFrag;
import com.parkingapp.hulapark.Fragments.MapFrag;
import com.parkingapp.hulapark.Fragments.ParkingFrag;
import com.parkingapp.hulapark.Fragments.WalletFrag;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Views.BottomNavMenuHolderView;
import com.parkingapp.hulapark.databinding.ActivityHomeBinding;

public class HomeScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavMenuHolderView mView = binding.BottomNavBar;
        mView.setSelectedItemId(R.id.nav_parking_car);
        mView.setOnNavigationItemSelectedListener(HomeScreen.this);
    }

    @Override
    public void onBackPressed() {
        finish(); // Close the app
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_parking_car:
                Navigation.findNavController(findViewById(R.id.activeFrag)).navigate(R.id.parkingFrag);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.activeFrag, ParkingFrag.class, null)
//                        .commit();
                break;
            case R.id.nav_map:
                Navigation.findNavController(findViewById(R.id.activeFrag)).navigate(R.id.mapFrag);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.activeFrag, MapFrag.class, null)
//                        .commit();
                break;
            case R.id.nav_wallet:
                Navigation.findNavController(findViewById(R.id.activeFrag)).navigate(R.id.walletFrag);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.activeFrag, WalletFrag.class, null)
//                        .commit();
                break;
            case R.id.nav_history:
                Navigation.findNavController(findViewById(R.id.activeFrag)).navigate(R.id.historyFrag);
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.activeFrag, HistoryFrag.class, null)
//                        .commit();
                break;
        }
        binding.Bubble.setX(findViewById(id).getX());
        return true;
    }
}