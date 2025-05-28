package com.parkingapp.hulapark.Utilities.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HulaMap
{
    private static HulaMap instance = null;
    private static MapView mapView;
    private static Context context;

    private HulaMap(MapView map)
    {
        mapView = map;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(12.0);
    }

    public static HulaMap Builder(MapView mapView)
    {
        instance = new HulaMap(mapView);
        return instance;
    }

    public static MapView getMap()
    {
        return mapView;
    }

    public HulaMap setMapFocalPoint(GeoPoint focalPoint, double zoomLevel)
    {
        mapView.getController().setCenter(focalPoint);
        mapView.getController().setZoom(zoomLevel);
        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(12.0);
        return this;
    }

    public static void setContext(Context applicationContext)
    {
        context = applicationContext;
    }

    public HulaMap loadMapMarkers(LayoutInflater inflater, HulaMapMarkerBehaviourModifier behaviourModifier)
    {
        for (Feature feature : CommonFragUtils.FragmentSwapper.getGeoLocModel().data.features)
        {
            HulaMapMarker.Builder(inflater, mapView, feature, behaviourModifier);
        }
        return this;
    }

    public void invalidateMap()
    {
        mapView.invalidate();
    }
}
