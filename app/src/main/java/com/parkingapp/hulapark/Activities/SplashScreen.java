package com.parkingapp.hulapark.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.databinding.ActivitySplashScreenBinding;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStuff();
        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Removing status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Setting splash animation
        binding.lottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                new Handler().post(() -> {
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

    private void initStuff()
    {
        FirebaseApp.initializeApp(getApplicationContext());
    }
}