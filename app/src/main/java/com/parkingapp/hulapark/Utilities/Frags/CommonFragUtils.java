package com.parkingapp.hulapark.Utilities.Frags;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.parkingapp.hulapark.Adapters.HistoryParkingCardAdapter;
import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
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

    private Object userClassType;

    public void changeUserTo(Object classType)
    {
        userClassType = classType;
        if (userClassType.getClass().getSimpleName().equals("User"))
            user_type.setValue(UserType.USER);
        else if(userClassType.getClass().getSimpleName().equals("Admin"))
            user_type.setValue(UserType.ADMIN);
        else
            user_type.setValue(UserType.GUEST);
    }

    public Object getUser() { return userClassType; }

    public MutableLiveData<UserType> getUserType()
    {
        return user_type;
    }

    private MutableLiveData<UserType> user_type = new MutableLiveData<>();
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
}
