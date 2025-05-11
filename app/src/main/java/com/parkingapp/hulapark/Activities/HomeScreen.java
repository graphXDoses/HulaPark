package com.parkingapp.hulapark.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.UserType;
import com.parkingapp.hulapark.databinding.ActivityHomeScreenBinding;

import java.util.HashMap;

public class HomeScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private ActivityHomeScreenBinding binding;
    private NavController navController;
    private final NavOptions.Builder navBuilder =  new NavOptions.Builder();
    private final HashMap<Integer, Integer> animatorMap = new HashMap<Integer, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navbar
        navController = Navigation.findNavController(findViewById(R.id.activeFrag));
        CommonFragUtils.FragmentSwapper.setNC_BottomNavMenu(navController);
        CommonFragUtils.FragmentSwapper.setBottomNavMenu(binding.BottomNavBar);
        binding.BottomNavBar.setSelectedItemId(R.id.nav_parking_car);
        binding.BottomNavBar.setOnNavigationItemSelectedListener(HomeScreen.this);

        // NavControllers
        NavController parking_nc = Navigation.findNavController(findViewById(R.id.parkingFragContainer));
        CommonFragUtils.FragmentSwapper.setNC_Parking(parking_nc);
        binding.switchUserType.setOnCheckedChangeListener((button, isChecked) -> {
            if(isChecked)
            {
                CommonFragUtils.FragmentSwapper.setUserType(UserType.USER);
            } else {
                CommonFragUtils.FragmentSwapper.setUserType(UserType.GUEST);
            }
            int id = CommonFragUtils.FragmentSwapper.getNC_BottomNavMenu().getCurrentDestination().getId();
            CommonFragUtils.FragmentSwapper.getNC_BottomNavMenu().navigate(id);
        });

        animatorMap.put(R.id.parkingFrag, 0);
        animatorMap.put(R.id.mapFrag, 1);
        animatorMap.put(R.id.walletFrag, 2);
        animatorMap.put(R.id.historyFrag, 3);
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
                navController.navigate(R.id.parkingFrag, null, setNavBuilderAnimations(R.id.parkingFrag));
                break;
            case R.id.nav_map:
                navController.navigate(R.id.mapFrag, null, setNavBuilderAnimations(R.id.mapFrag));
                break;
            case R.id.nav_wallet:
                navController.navigate(R.id.walletFrag, null, setNavBuilderAnimations(R.id.walletFrag));
                break;
            case R.id.nav_history:
                navController.navigate(R.id.historyFrag, null, setNavBuilderAnimations(R.id.historyFrag));
                break;
        }
        binding.BottomNavBar.setBubbleX(findViewById(id).getX());
        return true;
    }

    private NavOptions setNavBuilderAnimations(int targetFragment)
    {
        int currentFragID = animatorMap.get((int)navController.getCurrentDestination().getId());
        int targetFragID = animatorMap.get(targetFragment);
        int enter_anim = currentFragID > targetFragID ? R.anim.from_left : R.anim.from_right;
        int exit_anim = currentFragID > targetFragID ? R.anim.to_right : R.anim.to_left;

        navBuilder.setEnterAnim(enter_anim)
                .setExitAnim(exit_anim);

        return navBuilder.build();
    }
}