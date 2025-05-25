package com.parkingapp.hulapark.Activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Extras.ExtrasManager;
import com.parkingapp.hulapark.Views.TopNavMenuHolderView;
import com.parkingapp.hulapark.databinding.ActivityAuthScreenBinding;

import java.util.HashMap;

public class AuthScreen extends AppCompatActivity
{
    private TopNavMenuHolderView authNavMenuHolderView;
    private ActivityAuthScreenBinding binding;
    private NavController navController;
    private final NavOptions.Builder navBuilder =  new NavOptions.Builder();
    private Boolean isSignUpIntent = false;
    private final HashMap<Integer, Integer> animatorMap = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animatorMap.put(R.id.geustAuthSignInFrag, 0);
        animatorMap.put(R.id.guestAuthSignUpFrag, 1);

        findViewById(R.id.absBackBtn).setOnClickListener(view -> {
            finish();
        });

        authNavMenuHolderView = binding.authNavMenuHolderView;
        authNavMenuHolderView.attachIndicatorToSelection();


        ExtrasManager.getPassedExtras(savedInstanceState, getIntent(), (e) ->
        {
            isSignUpIntent= e.getBoolean("SIGNUP_INTENT");
        });

        navController = Navigation.findNavController(findViewById(R.id.authActiveFrag));

        authNavMenuHolderView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            int index = findItemIndex(id);
            authNavMenuHolderView.animateIndicatorToIndex(index);

            switch (id)
            {
                case(R.id.auth_nav_signin):
                {
                    navController.navigate(R.id.geustAuthSignInFrag, null, setNavBuilderAnimations(R.id.geustAuthSignInFrag));
                    break;
                }
                case(R.id.auth_nav_signup):
                {
                    navController.navigate(R.id.guestAuthSignUpFrag, null, setNavBuilderAnimations(R.id.guestAuthSignUpFrag));
                    break;
                }
            }

            return true;
        });

        authNavMenuHolderView.setSelectedItemId(isSignUpIntent ?R.id.auth_nav_signup : R.id.auth_nav_signin);
    }

    private int findItemIndex(int itemId) {
        Menu menu = authNavMenuHolderView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == itemId) return i;
        }
        return -1;
    }

    private NavOptions setNavBuilderAnimations(int targetFragment)
    {
        int currentDestID = (int)navController.getCurrentDestination().getId();
        int currentFragID = animatorMap.get(currentDestID);
        int targetFragID = animatorMap.get(targetFragment);
        int enter_anim = currentFragID > targetFragID ? R.anim.from_left : R.anim.from_right;
        int exit_anim = currentFragID > targetFragID ? R.anim.to_right : R.anim.to_left;

        return navBuilder.setEnterAnim(enter_anim)
                         .setExitAnim(exit_anim)
                         .setPopUpTo(currentDestID, true)
                         .build();
    }
}