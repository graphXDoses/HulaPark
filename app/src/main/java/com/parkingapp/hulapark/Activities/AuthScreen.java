package com.parkingapp.hulapark.Activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Views.TopNavMenuHolderView;

public class AuthScreen extends AppCompatActivity
{
    private TopNavMenuHolderView topNavMenuHolderView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_screen);

        topNavMenuHolderView = (TopNavMenuHolderView)findViewById(R.id.authNavMenuHolderView);
        topNavMenuHolderView.attachIndicatorToSelection();

        Boolean isSignUpIntent = false;

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                isSignUpIntent= false;
            } else {
                isSignUpIntent= extras.getBoolean("SIGNUP_INTENT");
            }
        }

        topNavMenuHolderView.setOnNavigationItemSelectedListener(item -> {
            int index = findItemIndex(item.getItemId());
            topNavMenuHolderView.animateIndicatorToIndex(index);
            return true;
        });

        topNavMenuHolderView.setSelectedItemId(isSignUpIntent ?R.id.auth_nav_signup : R.id.auth_nav_signin);
    }

    private int findItemIndex(int itemId) {
        Menu menu = topNavMenuHolderView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == itemId) return i;
        }
        return -1;
    }
}