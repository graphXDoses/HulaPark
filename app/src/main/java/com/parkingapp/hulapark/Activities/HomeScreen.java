package com.parkingapp.hulapark.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.databinding.ActivityHomeBinding;

public class HomeScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private ActivityHomeBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navbar
        navController = Navigation.findNavController(findViewById(R.id.activeFrag));
        CommonFragUtils.FragmentSwapper.setNavController(navController);
        CommonFragUtils.FragmentSwapper.setBottomNavMenu(binding.BottomNavBar);
        binding.BottomNavBar.setSelectedItemId(R.id.nav_parking_car);
        binding.BottomNavBar.setOnNavigationItemSelectedListener(HomeScreen.this);
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
                navController.navigate(R.id.parkingFrag);
                break;
            case R.id.nav_map:
                navController.navigate(R.id.mapFrag);
                break;
            case R.id.nav_wallet:
                navController.navigate(R.id.walletFrag);
                break;
            case R.id.nav_history:
                navController.navigate(R.id.historyFrag);
                break;
        }
        binding.BottomNavBar.setBubbleX(findViewById(id).getX());
//        binding.Bubble.setX(findViewById(id).getX());
        return true;
    }
}