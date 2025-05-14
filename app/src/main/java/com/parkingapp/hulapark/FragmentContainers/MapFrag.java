package com.parkingapp.hulapark.FragmentContainers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.GeoJsonModels.Feature;
import com.parkingapp.hulapark.Utilities.GeoJsonModels.GeoJsonData;
import com.parkingapp.hulapark.WarningDialogBox;

import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MapView map;
    public MapFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFrag newInstance(String param1, String param2) {
        MapFrag fragment = new MapFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Configuration.getInstance().load(getContext().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_map, container, false);

        view.findViewById(R.id.mapFindMyLocation).setOnClickListener(v ->
        {
            new WarningDialogBox(getContext()).builder()
                    .setDescription(getString(R.string.locationFinderNotImplemented))
                    .show();
        });

        map = view.findViewById(R.id.streetMap);

        loadMap(map);
        List<GeoPoint> parkingSpots = parseGeoJsonData(R.raw.parkingspots);
        loadMapMarkers(parkingSpots);

        map.invalidate();

        return view;
    }

    private void loadMap(MapView mapView){
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        GeoPoint startPoint = new GeoPoint(21.3123319465878, -157.860639202242);
        map.getController().setZoom(12.28);
        map.getController().setCenter(startPoint);
    }

    private List<GeoPoint> parseGeoJsonData(final int resourceID) {
        List<GeoPoint> geoPoints = new ArrayList<>();

        try {
            InputStream is = getResources().openRawResource(resourceID);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Gson gson = new Gson();

            GeoJsonData geoData = gson.fromJson(reader, GeoJsonData.class);

            for (Feature feature : geoData.features) {
                if ("Point".equals(feature.geometry.type)) {
                    List<Double> coords = feature.geometry.coordinates;
                    double lon = coords.get(0);
                    double lat = coords.get(1);
                    geoPoints.add(new GeoPoint(lat, lon));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading GeoJSON: " + e.getMessage());
        }

        return geoPoints;
    }

    private void loadMapMarkers(List<GeoPoint> geoPoints) {
        for (GeoPoint point : geoPoints) {
            Marker marker = new Marker(map);

            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener((m, mapView) -> {
                m.showInfoWindow();
                return true;
            });

            map.getOverlays().add(marker);
        }
    }
}