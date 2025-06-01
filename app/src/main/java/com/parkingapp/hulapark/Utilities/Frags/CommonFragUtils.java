package com.parkingapp.hulapark.Utilities.Frags;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.parkingapp.hulapark.Activities.HomeScreen;
import com.parkingapp.hulapark.Adapters.HistoryParkingCardAdapter;
import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Utilities.Users.UserType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CommonFragUtils
{
    private CommonFragUtils() { }
    public static final CommonFragUtils FragmentSwapper = new CommonFragUtils();

    public List<Feature> getMapFeatures()
    {
        return mapFeatures;
    }

    public void setMapFeatures(List<Feature> mapFeatures)
    {
        this.mapFeatures = mapFeatures;
    }

    private List<Feature> mapFeatures;
    private String mapFocusedPoint = null;

    public HomeScreen getHomeActivity()
    {
        return homeActivity;
    }

    public void setHomeActivity(HomeScreen homeActivity)
    {
        this.homeActivity = homeActivity;
    }

    private HomeScreen homeActivity;

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

    public GeoJsonDataModel getGeoDataModel()
    {
        return geoJsonDataModel;
    }
    public void setGeoDataModel(GeoJsonDataModel model)
    {
        geoJsonDataModel = model;
    }

    public void createLocalGeoDataModelGeoJson(final int resID, Context context)
    {
        InputStream is = context.getResources().openRawResource(resID);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Gson gson = new Gson();

        FeatureCollection collection = gson.fromJson(reader, FeatureCollection.class);

        geoJsonDataModel = new GeoJsonDataModel();
        geoJsonDataModel.GeoData = collection;
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
