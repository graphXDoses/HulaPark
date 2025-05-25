package com.parkingapp.hulapark.Utilities.Frags;

import android.content.Context;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.parkingapp.hulapark.Adapters.HistoryParkingCardAdapter;
import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Utilities.Users.UserType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonFragUtils
{
    public static final CommonFragUtils FragmentSwapper = new CommonFragUtils();
    public static final int hourScale = 24;

    private CommonFragUtils() { }
    private NavController NC_BottomNavMenu;
    private GeoJsonDataModel geoJsonDataModel;

    public void changeUserTo(Object classType)
    {
        if (classType.getClass().getSimpleName().equals("User"))
            user_type = UserType.USER;
        else
            user_type = UserType.GUEST;
    }

    private UserType user_type = UserType.GUEST;

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
}
