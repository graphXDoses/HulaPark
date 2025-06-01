package com.parkingapp.hulapark.Utilities.Map;

import android.content.Context;
import android.view.LayoutInflater;

import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.util.List;

public class HulaMap
{
    private MapView mapView;
    private Context context;

    public HulaMap(MapView map)
    {
        mapView = map;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(12.0);
    }

    public MapView getMap()
    {
        return mapView;
    }

    public HulaMap setMapFocalPoint(GeoPoint focalPoint, double zoomLevel)
    {
        mapView.getController().setCenter(focalPoint);
        mapView.getController().setZoom(zoomLevel);
        return this;
    }

    public void setContext(Context applicationContext)
    {
        context = applicationContext;
    }

    public HulaMap loadMapMarkers(LayoutInflater inflater, List<Feature> features, HulaMapMarkerBehaviourModifier behaviourModifier)
    {
        for (Feature feature : features)
        {
            HulaMapMarker.Builder(inflater, mapView, feature, behaviourModifier);
        }
        return this;
    }

    public HulaMap loadMapMarkers()
    {
        for (Feature feature : CommonFragUtils.FragmentSwapper.getGeoDataModel().GeoData.features)
        {
            HulaMapMarker.Builder(mapView, feature);
        }
        return this;
    }

    public void invalidateMap()
    {
        mapView.invalidate();
    }

    public void flyToMapFocalPoint(GeoPoint geoPoint, double v)
    {
        mapView.getController().animateTo(geoPoint, 16.0, 750L);
    }
}
