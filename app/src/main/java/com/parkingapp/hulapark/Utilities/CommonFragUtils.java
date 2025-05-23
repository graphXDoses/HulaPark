package com.parkingapp.hulapark.Utilities;

import android.content.Context;

import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Views.BottomNavMenuHolderView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonFragUtils
{
    public static final CommonFragUtils FragmentSwapper = new CommonFragUtils();
    private CommonFragUtils() { }
    private BottomNavMenuHolderView bottomNavMenu;
    private NavController NC_BottomNavMenu;
    private GeoJsonDataModel geoJsonDataModel;

    public UserType getUserType() {
        return user_type;
    }

    public void setUserType(UserType type) {
        user_type = type;
    }

    private UserType user_type = UserType.GUEST;

//    public NavController getNC_Parking() {
//        return NC_Parking;
//    }

//    public void setNC_Parking(NavController NC_Parking) {
//        this.NC_Parking = NC_Parking;
//    }

//    private NavController NC_Parking;

//    public BottomNavMenuHolderView getBottomNavMenu() {
//        return bottomNavMenu;
//    }
//
//    public void setBottomNavMenu(BottomNavMenuHolderView bottomNavMenu)
//    {
//        this.bottomNavMenu = bottomNavMenu;
//    }

    public void setNC_BottomNavMenu(NavController NC_BottomNavMenu)
    {
        this.NC_BottomNavMenu = NC_BottomNavMenu;
    }

    public NavController getNC_BottomNavMenu()
    {
        return NC_BottomNavMenu;
    }

    public GeoJsonDataModel getGeoLocModel()
    {
        return geoJsonDataModel;
    }

    public void createGeoLocModelFromGeoJson(final int resID, Context context)
    {
        InputStream is = context.getResources().openRawResource(resID);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Gson gson = new Gson();

        FeatureCollection collection = gson.fromJson(reader, FeatureCollection.class);

        geoJsonDataModel = new GeoJsonDataModel();
        geoJsonDataModel.data = collection;
    }
}
