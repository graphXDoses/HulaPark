package com.parkingapp.hulapark;

import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parkingapp.hulapark.Views.BottomNavMenuHolderView;

public class CommonFragUtils
{
    public static final CommonFragUtils FragmentSwapper = new CommonFragUtils();
    private CommonFragUtils() { }
    private NavController navController;
    private BottomNavMenuHolderView bottomNavMenu;

    public BottomNavMenuHolderView getBottomNavMenu() {
        return bottomNavMenu;
    }

    public void setBottomNavMenu(BottomNavMenuHolderView bottomNavMenu)
    {
        this.bottomNavMenu = bottomNavMenu;
    }

    public void setNavController(NavController navController)
    {
        this.navController = navController;
    }

    public NavController getNavController()
    {
        return navController;
    }
}
