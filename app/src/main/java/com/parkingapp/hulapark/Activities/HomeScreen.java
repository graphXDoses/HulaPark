package com.parkingapp.hulapark.Activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.DialogBoxes.OKDialogBox;
import com.parkingapp.hulapark.Utilities.Extras.ExtrasManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.DialogBoxes.WarningDialogBox;
import com.parkingapp.hulapark.Utilities.Frags.IActiveUserFragSetter;
import com.parkingapp.hulapark.Utilities.Users.UserType;
import com.parkingapp.hulapark.databinding.ActivityHomeScreenBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, IActiveUserFragSetter
{
    private ActivityHomeScreenBinding binding;
    private NavController mainNavController;
    private NavController topbarNavController;
    private Bundle bottomNavBarArgs = null;
    private final NavOptions.Builder navBuilder =  new NavOptions.Builder();
    private final HashMap<Integer, Integer> animatorMap = new HashMap<Integer, Integer>();
    private static final int BACK_PRESS_INTERVAL = 2000;
    private long lastBackPressedTime = 0;
    private Toast backToast;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable resetBackPressedFlag;
    private Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // User Fragment Setup
        userFragmentSetup();

        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CommonFragUtils.FragmentSwapper.setHomeActivity(this);
        this.savedInstanceState = savedInstanceState;

//        sharedPreferences = getSharedPreferences("SWITCH_STATE", MODE_PRIVATE);

        // Navbar
        mainNavController = Navigation.findNavController(findViewById(R.id.activeFrag));

        topbarNavController = Navigation.findNavController(findViewById(R.id.topbarFragContainer));

        navToSelectedFragUpdateBottomNavBar(R.id.nav_parking_car, null);
        binding.BottomNavBar.setOnNavigationItemSelectedListener(HomeScreen.this);

        CommonFragUtils.FragmentSwapper.authActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        Boolean shouldNavigate = data.getBooleanExtra("shouldNavigate", false);

                        if(shouldNavigate)
                        {
                            int id = mainNavController.getCurrentDestination().getId();
                            mainNavController.navigate(id);
                            topbarNavController.navigate(R.id.userTopbarState);
                        }
                    }
                }
        );

        WarningDialogBox.setBackground(getDrawable(R.drawable.bg_info_panel));
        OKDialogBox.setBackground(getDrawable(R.drawable.bg_info_panel));
        CommonFragUtils.FragmentSwapper.createLocalGeoDataModelGeoJson(R.raw.parkingspots, getApplicationContext());

        CommonFragUtils.FragmentSwapper.getUserType().observe(this, userType -> {
            setActiveUserFrag(userType, binding.getRoot(), R.id.topbarFragContainer);
            if(userType == UserType.GUEST)
            {
                CommonFragUtils.FragmentSwapper.getParkingCardAdapter().setCards(new ArrayList<>());
                CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter().setCards(new ArrayList<>());
                CommonFragUtils.FragmentSwapper.setMapFeatures(CommonFragUtils.FragmentSwapper.getGeoDataModel().GeoData.features);
            }
        });

        animatorMap.put(R.id.parkingFrag, 0);
        animatorMap.put(R.id.mapFrag, 1);
        animatorMap.put(R.id.walletFrag, 2);
        animatorMap.put(R.id.statisticsFrag, 3);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        ExtrasManager.getPassedExtras(savedInstanceState, intent, (e) ->
        {
            int gotoPage = e.getInt("GOTO_PAGE");
            navToSelectedFragUpdateBottomNavBar(gotoPage, null);
        });
    }

    public void navToSelectedFragUpdateBottomNavBar(int bottom_nav_menu_item, Bundle args)
    {
        bottomNavBarArgs = args;
        binding.BottomNavBar.setSelectedItemId(bottom_nav_menu_item);
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

        if(id == binding.BottomNavBar.getSelectedItemId())
            return true;

        switch (id) {
            case R.id.nav_parking_car:
                mainNavController.navigate(R.id.parkingFrag, bottomNavBarArgs, setNavBuilderAnimations(R.id.parkingFrag));
                break;
            case R.id.nav_map:
                mainNavController.navigate(R.id.mapFrag, bottomNavBarArgs, setNavBuilderAnimations(R.id.mapFrag));
                break;
            case R.id.nav_wallet:
                mainNavController.navigate(R.id.walletFrag, bottomNavBarArgs, setNavBuilderAnimations(R.id.walletFrag));
                break;
            case R.id.nav_statistics:
                mainNavController.navigate(R.id.statisticsFrag, bottomNavBarArgs, setNavBuilderAnimations(R.id.statisticsFrag));
                break;
        }
        binding.BottomNavBar.setBubbleX(findViewById(id).getX());
        bottomNavBarArgs = null;
        return true;
    }

    private NavOptions setNavBuilderAnimations(int targetFragment)
    {
        int currentFragID = animatorMap.get((int)mainNavController.getCurrentDestination().getId());
        int targetFragID = animatorMap.get(targetFragment);
        int enter_anim = currentFragID > targetFragID ? R.anim.from_left : R.anim.from_right;
        int exit_anim = currentFragID > targetFragID ? R.anim.to_right : R.anim.to_left;

        navBuilder.setEnterAnim(enter_anim)
                .setExitAnim(exit_anim);

        return navBuilder.build();
    }

    @Override
    public void setActiveUserFrag(UserType userType, View thisView, int fragContainer)
    {
        switch (userType)
        {
            case GUEST:
            {
                topbarNavController.navigate(R.id.guestTopbarState);
                break;
            }
            case USER:
            {
                topbarNavController.navigate(R.id.userTopbarState);
                break;
            }
        }
    }
}