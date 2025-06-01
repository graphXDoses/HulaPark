package com.parkingapp.hulapark.Utilities.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HulaMapMarker extends Marker
{
    private Context context;
    private BottomSheetDialog bottomSheetDialog;

    private static HulaMapMarker instance = null;

    private static HashMap<String, HulaMapMarker> sectorIDMap = new HashMap<>();

    private HulaMapMarkerOnClickListener listener;
    private MapView mapView;

    public OnMarkerClickListener getOnMarkerClickListener()
    {
        return listener;
    }
    private HulaMapMarker(LayoutInflater inflater, MapView mapView, Feature feature, HulaMapMarkerBehaviourModifier behaviourMod)
    {
        super(mapView);
        this.mapView = mapView;
        this.context = mapView.getContext();
        this.bottomSheetDialog = new BottomSheetDialog(context);

        // HMM
        HMM descriptor = new HMM();

        List<Double> co = feature.geometry.coordinates;
        sectorIDMap.put(feature.properties.sectorID, this);

        descriptor.geoLocationPoint = new GeoPoint(co.get(1), co.get(0));
        descriptor.inflater = inflater;
        descriptor.behaviourMod = behaviourMod;
        descriptor.context = mapView.getContext();
        descriptor.markFeatures = new HashMap<>();
        descriptor.locationCircle = context.getResources().getDrawable(R.drawable.map_location_circle);
        descriptor.locationParkingActive = context.getResources().getDrawable(R.drawable.map_location_parking_active);

        // Set point style
        setPosition(descriptor.geoLocationPoint);
        setIcon(descriptor.locationCircle);
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        // Set onclick listener
        listener = new HulaMapMarkerOnClickListener(descriptor);
        setOnMarkerClickListener(listener);

        descriptor.markFeatures.put(this, feature);
        mapView.getOverlays().add(this);
    }

    private HulaMapMarker(MapView mapView, Feature feature)
    {
        super(mapView);
        this.mapView = mapView;
        this.context = mapView.getContext();
        this.bottomSheetDialog = new BottomSheetDialog(context);

        List<Double> co = feature.geometry.coordinates;
        sectorIDMap.put(feature.properties.sectorID, this);

        // Set point style
        setPosition(new GeoPoint(co.get(1), co.get(0)));
        setIcon(context.getResources().getDrawable(R.drawable.map_location_circle));
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(this);
    }

    public static HulaMapMarker Builder(LayoutInflater inflater, MapView mapView, Feature feature, HulaMapMarkerBehaviourModifier behaviourMod)
    {
        instance = new HulaMapMarker(inflater, mapView, feature, behaviourMod);
        return instance;
    }

    public static HulaMapMarker Builder(MapView mapView, Feature feature)
    {
        instance = new HulaMapMarker(mapView, feature);
        return instance;
    }

    public static HulaMapMarker getMarkerBySectorID(String s)
    {
        HulaMapMarker marker = sectorIDMap.get(s);
        return marker;
    }

    private class HMM
    {
        public Drawable locationCircle;
        public Drawable locationParkingActive;
        public GeoPoint geoLocationPoint;
        public HulaMapMarkerBehaviourModifier behaviourMod;
        public HashMap<Marker, Feature> markFeatures;
        public LayoutInflater inflater;
        public Context context;
    }

    private class HulaMapMarkerOnClickListener implements OnMarkerClickListener
    {
        private HMM descriptor;
        public HulaMapMarkerOnClickListener(HMM descriptor)
        {
            this.descriptor = descriptor;
        }

        @Override
        public boolean onMarkerClick(Marker m, MapView map)
        {
            Feature f = descriptor.markFeatures.get(m);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(descriptor.context);
            View view = descriptor.inflater.from(descriptor.context).inflate(R.layout.frag_map_display_spot_details, null);

            FrameLayout dynamicLayout = view.findViewById(R.id.displayDynamicFrameLayout);

            AtomicInteger layoutToInflate = new AtomicInteger(0);
            descriptor.behaviourMod.chooseLayout(layoutToInflate, f);
            if(layoutToInflate.get() != 0)
                LayoutInflater.from(descriptor.context).inflate(layoutToInflate.get(), dynamicLayout, true);

            descriptor.behaviourMod.afterInflating(layoutToInflate.get(), dynamicLayout, view, bottomSheetDialog);

            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();

            map.getOverlays().remove(m);
            m.setIcon(descriptor.locationParkingActive);
            map.getOverlays().add(m);
            map.invalidate();

            double previousZoomLevel = map.getZoomLevelDouble();
//            GeoPoint previousFocalPoint = (GeoPoint)map.getMapCenter();
            GeoPoint previousFocalPoint = new GeoPoint(21.309884, -157.858140);
            map.getController().animateTo(
                    descriptor.geoLocationPoint,
                    16.0,
                    750L);

            ((TextView)view.findViewById(R.id.displaySpotAddress)).setText(f.properties.address);
            ((TextView)view.findViewById(R.id.displaySectorID)).setText(f.properties.sectorID);

//            ((MaterialButton)view.findViewById(R.id.displaySpotDismissBtn)).setOnClickListener(__ -> {
//                bottomSheetDialog.dismiss();
//                mapView.getController().animateTo(
//                        point,
//                        previousZoomLevel,
//                        750L);
//            });

            bottomSheetDialog.setOnDismissListener(dialogInterface -> {
                m.setIcon(descriptor.locationCircle);
                map.getController().animateTo(
                        previousFocalPoint,
                        previousZoomLevel,
                        750L);
                map.invalidate();
            });
            return true;
        }
    }
}