package com.parkingapp.hulapark.Utilities.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OsmMapModifier
{
    private MapView mapView;
    private static Context context;

    public OsmMapModifier(MapView osmap)
    {
        this.mapView = osmap;
    }

    public static void setContext(Context applicationContext)
    {
        context = applicationContext;
    }

    public OsmMapModifier Builder()
    {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        return this;
    }

    public GeoJsonDataModel loadGeoJsonData(final int resID)
    {
        InputStream is = context.getResources().openRawResource(resID);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Gson gson = new Gson();

        FeatureCollection collection = gson.fromJson(reader, FeatureCollection.class);

        GeoJsonDataModel geoJsonDataModel = new GeoJsonDataModel();
        geoJsonDataModel.data = collection;

        return geoJsonDataModel;
    }

    public OsmMapModifier setInitialCameraAngle(float zoom, double Lat, double Lon)
    {
        mapView.getController().setCenter(new GeoPoint(Lat, Lon));
        mapView.getController().setZoom(zoom);
        return this;
    }
}
