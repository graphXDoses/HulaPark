package com.parkingapp.hulapark;

import androidx.navigation.NavController;

import com.parkingapp.hulapark.Views.BottomNavMenuHolderView;

public class CommonFragUtils
{
    public static final CommonFragUtils FragmentSwapper = new CommonFragUtils();
    private CommonFragUtils() { }
    private BottomNavMenuHolderView bottomNavMenu;
    private NavController NC_BottomNavMenu;

    public UserType getUserType() {
        return user_type;
    }

    public void setUserType(UserType type) {
        user_type = type;
    }

    private UserType user_type = UserType.GUEST;

    public NavController getNC_Parking() {
        return NC_Parking;
    }

    public void setNC_Parking(NavController NC_Parking) {
        this.NC_Parking = NC_Parking;
    }

    private NavController NC_Parking;

    public BottomNavMenuHolderView getBottomNavMenu() {
        return bottomNavMenu;
    }

    public void setBottomNavMenu(BottomNavMenuHolderView bottomNavMenu)
    {
        this.bottomNavMenu = bottomNavMenu;
    }

    public void setNC_BottomNavMenu(NavController NC_BottomNavMenu)
    {
        this.NC_BottomNavMenu = NC_BottomNavMenu;
    }

    public NavController getNC_BottomNavMenu()
    {
        return NC_BottomNavMenu;
    }
}
