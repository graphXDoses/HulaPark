package com.parkingapp.hulapark.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.ExtrasManager;
import com.parkingapp.hulapark.Views.TopNavMenuHolderView;
import com.parkingapp.hulapark.databinding.ActivityAuthScreenBinding;

import java.util.function.Consumer;
import java.util.function.Function;

public class AuthScreen extends AppCompatActivity
{
    private TopNavMenuHolderView authNavMenuHolderView;
    private ActivityAuthScreenBinding binding;
    private NavController navController;
    private Boolean isSignUpIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            int currentDest = navController.getCurrentDestination().getId();
            authNavMenuHolderView.animateIndicatorToIndex(index);

            switch (id)
            {
                case(R.id.auth_nav_signin):
                {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(currentDest, true)
                            .build();

                    navController.navigate(R.id.geustAuthSignInFrag, null, navOptions);
                    break;
                }
                case(R.id.auth_nav_signup):
                {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(currentDest, true)
                            .build();

                    navController.navigate(R.id.guestAuthSignUpFrag, null, navOptions);
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
}