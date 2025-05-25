package com.parkingapp.hulapark.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.DialogBoxes.WarningDialogBox;
import com.parkingapp.hulapark.databinding.ActivityHomeScreenBinding;

import java.util.HashMap;

public class HomeScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private ActivityHomeScreenBinding binding;
    private NavController navController;
    private final NavOptions.Builder navBuilder =  new NavOptions.Builder();
    private final HashMap<Integer, Integer> animatorMap = new HashMap<Integer, Integer>();
    private static final int BACK_PRESS_INTERVAL = 2000;
    private long lastBackPressedTime = 0;
    private Toast backToast;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable resetBackPressedFlag;
    private SharedPreferences sharedPreferences;

    private final String IS_TOGGLE = "IS_TOGGLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // User Fragment Setup
        userFragmentSetup();

        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("SWITCH_STATE", MODE_PRIVATE);

        // Navbar
        navController = Navigation.findNavController(findViewById(R.id.activeFrag));
        CommonFragUtils.FragmentSwapper.setNC_BottomNavMenu(navController);
        binding.BottomNavBar.setSelectedItemId(R.id.nav_parking_car);
        binding.BottomNavBar.setOnNavigationItemSelectedListener(HomeScreen.this);



        // NavControllers
//        NavController parking_nc = Navigation.findNavController(findViewById(R.id.parkingFragContainer));
//        CommonFragUtils.FragmentSwapper.setNC_Parking(parking_nc);
        binding.switchUserType.setOnCheckedChangeListener((button, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(isChecked)
            {
                CommonFragUtils.FragmentSwapper.changeUserTo(new User());
                editor.putBoolean(IS_TOGGLE, true);
            } else {
                CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
                editor.putBoolean(IS_TOGGLE, false);
            }
            editor.commit();
            int id = CommonFragUtils.FragmentSwapper.getNC_BottomNavMenu().getCurrentDestination().getId();
            CommonFragUtils.FragmentSwapper.getNC_BottomNavMenu().navigate(id);
        });

        binding.switchUserType.setChecked(sharedPreferences.getBoolean(IS_TOGGLE, false));

        WarningDialogBox.setBackground(getDrawable(R.drawable.bg_info_panel));
        CommonFragUtils.FragmentSwapper.createGeoLocModelFromGeoJson(R.raw.parkingspots, getApplicationContext());

        animatorMap.put(R.id.parkingFrag, 0);
        animatorMap.put(R.id.mapFrag, 1);
        animatorMap.put(R.id.walletFrag, 2);
        animatorMap.put(R.id.statisticsFrag, 3);
    }

    private void userFragmentSetup()
    {
        // Parking
        Guest.setFragmentContainerActiveFrag(R.id.parkingFragContainer, R.id.guestParkingFrag);
        User.setFragmentContainerActiveFrag(R.id.parkingFragContainer, R.id.userParkingFrag);
        // Wallet
        Guest.setFragmentContainerActiveFrag(R.id.walletFragContainer, R.id.guestWalletFrag);
        User.setFragmentContainerActiveFrag(R.id.walletFragContainer, R.id.userWalletFrag);
        // Stats
        Guest.setFragmentContainerActiveFrag(R.id.statistcsFragContainer, R.id.guestStatisticsFrag);
        User.setFragmentContainerActiveFrag(R.id.statistcsFragContainer, R.id.userStatisticsFrag);
    }

    @Override
    public void onBackPressed()
    {
        long currentTime = System.currentTimeMillis();
        backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);

        if (currentTime - lastBackPressedTime < BACK_PRESS_INTERVAL) {
            // Avoid stacking
            if (backToast != null) backToast.cancel();
            handler.removeCallbacks(resetBackPressedFlag);
            finish();
        } else {
            lastBackPressedTime = currentTime;
            if (backToast != null) backToast.cancel();
            backToast.show();

            // Reset the flag after the toast duration
            if (resetBackPressedFlag == null) {
                resetBackPressedFlag = () -> lastBackPressedTime = 0;
            }
            handler.removeCallbacks(resetBackPressedFlag);
            handler.postDelayed(resetBackPressedFlag, BACK_PRESS_INTERVAL);
        }
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
            case R.id.nav_statistics:
                navController.navigate(R.id.statisticsFrag, null, setNavBuilderAnimations(R.id.statisticsFrag));
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