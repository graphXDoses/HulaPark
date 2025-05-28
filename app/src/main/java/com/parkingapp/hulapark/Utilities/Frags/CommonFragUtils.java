package com.parkingapp.hulapark.Utilities.Frags;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.parkingapp.hulapark.Adapters.HistoryParkingCardAdapter;
import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
import com.parkingapp.hulapark.Users.AUser;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Utilities.Users.UserType;
import com.parkingapp.hulapark.Views.BottomNavMenuHolderView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonFragUtils
{
    public static final CommonFragUtils FragmentSwapper = new CommonFragUtils();
    public static final int hourScale = 1;
    private String mapFocusedPoint = null;
    private LifecycleOwner homeActivityLifecycleOwner;

    public BottomNavMenuHolderView getBottomNavBar()
    {
        return bottomNavBar;
    }

    private BottomNavMenuHolderView bottomNavBar;

    private CommonFragUtils() { }
    private NavController NC_BottomNavMenu;
    private GeoJsonDataModel geoJsonDataModel;

    private AUser userClassType;

    public void changeUserTo(AUser classType)
    {
        userClassType = classType;
        if (userClassType.getClass().getSimpleName().equals("User"))
            user_type = UserType.USER;
        else
            user_type = UserType.GUEST;
    }

    public AUser getUser() { return userClassType; }

    private UserType user_type = UserType.GUEST;
    public ActivityResultLauncher<Intent> authActivityLauncher;

    private final OngoingParkingCardAdapter ongoingParkingCardAdapter = new OngoingParkingCardAdapter();
    private final HistoryParkingCardAdapter historyParkingCardAdapter = new HistoryParkingCardAdapter();

    public OngoingParkingCardAdapter getParkingCardAdapter()
    {
        return ongoingParkingCardAdapter;
    }

    public HistoryParkingCardAdapter getHistoryParkingCardAdapter()
    {
        return historyParkingCardAdapter;
    }

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

    public void setActiveUserFrag(View view, int fragContainer)
    {
        NavController navController = Navigation.findNavController(view.findViewById(fragContainer));
        switch (user_type)
        {
            case GUEST:
            {
                navController.navigate(Guest.getFragmentContainerActiveFrag(fragContainer));
                break;
            }
            case USER:
            {
                navController.navigate(User.getFragmentContainerActiveFrag(fragContainer));
                break;
            }
        }
    }

    public void setBottomNavBar(BottomNavMenuHolderView bottomNavBar)
    {
        this.bottomNavBar = bottomNavBar;
    }

    public void setMapFocusedPoint(String s)
    {
        mapFocusedPoint = s;
    }

    public String getMapFocusedPoint()
    {
        return mapFocusedPoint;
    }

    public LifecycleOwner getHomeActivityLifecycleOwner()
    {
        return homeActivityLifecycleOwner;
    }

    public void setHomeActivityLifecycleOwner(LifecycleOwner owner)
    {
        homeActivityLifecycleOwner = owner;
    }
}
