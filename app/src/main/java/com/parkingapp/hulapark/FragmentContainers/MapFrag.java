package com.parkingapp.hulapark.FragmentContainers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parkingapp.hulapark.R;
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
        Configuration.getInstance().load(getContext().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext()));

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_map, container, false);

        map = view.findViewById(R.id.streetMap);

        view.findViewById(R.id.mapFindMyLocation).setOnClickListener(v ->
        {
            new WarningDialogBox(getContext()).builder()
                    .setDescription(getString(R.string.locationFinderNotImplemented))
                    .show();
        });

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        GeoPoint startPoint = new GeoPoint(21.3123319465878, -157.860639202242); // San Francisco
        map.getController().setZoom(12.28);
        map.getController().setCenter(startPoint);
        loadGeoJsonFromAssets();

        return view;
    }

    private void loadGeoJsonFromAssets() {
        try {
            InputStream is = getResources().openRawResource(R.raw.parkingspots);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String geoJsonStr = builder.toString();
            JSONObject geoJson = new JSONObject(geoJsonStr);
            JSONArray features = geoJson.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                String type = geometry.getString("type");

                if ("Point".equals(type)) {
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    double lon = coordinates.getDouble(0);
                    double lat = coordinates.getDouble(1);

                    System.out.println("Adding marker at: " + lat + ", " + lon);

                    Marker marker = new Marker(map);
                    marker.setPosition(new GeoPoint(lat, lon));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    JSONObject properties = feature.getJSONObject("properties");
                    String name = properties.optString("name", "Unknown");
                    String description = properties.optString("description", "No description available.");

                    marker.setTitle(name);
                    marker.setSubDescription(description);

                    // Optional: show the info window when marker is clicked
                    marker.setOnMarkerClickListener((Marker m, MapView mapView) -> {
                        m.showInfoWindow();
                        return true;
                    });
                    map.getOverlays().add(marker);
                }
            }

            map.invalidate();

        } catch (IOException | JSONException e) {
            System.out.println("Error loading GeoJSON: ");
        }
    }
}