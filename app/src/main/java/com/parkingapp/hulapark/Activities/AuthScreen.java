package com.parkingapp.hulapark.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Views.AuthNavMenuHolderView;

public class AuthScreen extends AppCompatActivity
{
    private AuthNavMenuHolderView authNavMenuHolderView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_screen);

        authNavMenuHolderView = (AuthNavMenuHolderView)findViewById(R.id.authNavMenuHolderView);
        authNavMenuHolderView.attachIndicatorToSelection();

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

        authNavMenuHolderView.setOnNavigationItemSelectedListener(item -> {
            int index = findItemIndex(item.getItemId());
            authNavMenuHolderView.animateIndicatorToIndex(index);
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