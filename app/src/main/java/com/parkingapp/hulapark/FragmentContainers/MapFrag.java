package com.parkingapp.hulapark.FragmentContainers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Utilities.DialogBoxes.WarningDialogBox;

import android.preference.PreferenceManager;
import android.widget.TextView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
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

    private HashMap<Marker, Feature> markFeatures;
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
        GeoJsonDataModel model = CommonFragUtils.FragmentSwapper.getGeoLocModel();

        loadMapMarkers(model.data.features);
        map.invalidate();



        return view;
    }

    private void loadMap(MapView mapView)
    {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        mapView.getController().setCenter(new GeoPoint(21.309884, -157.858140));
        mapView.getController().setZoom(13.0);
        mapView.setMaxZoomLevel(20.0);
        mapView.setMinZoomLevel(12.0);
    }

    private void loadMapMarkers(List<Feature> features)
    {
        markFeatures = new HashMap<>();
        for (Feature feature : features)
        {
            Marker marker = new Marker(map);
            List<Double> co = feature.geometry.coordinates;

            GeoPoint point = new GeoPoint(co.get(1), co.get(0));
            marker.setPosition(point);
            marker.setIcon(getResources().getDrawable(R.drawable.map_location_circle));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setOnMarkerClickListener((m, mapView) -> {
                Feature f = markFeatures.get(m);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                View view = getLayoutInflater().from(getContext()).inflate(R.layout.frag_map_display_spot_details, null);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

                map.getOverlays().remove(m);
                m.setIcon(getResources().getDrawable(R.drawable.map_location_parking_active));
                map.getOverlays().add(m);
                map.invalidate();

                double previousZoomLevel = map.getZoomLevelDouble();
                mapView.getController().animateTo(
                        point,
                        16.0,
                        750L);

                ((TextView)view.findViewById(R.id.displaySpotAddress)).setText(f.properties.address);
                ((TextView)view.findViewById(R.id.displaySpotID)).setText(f.properties.sectorID);

                ((MaterialButton)view.findViewById(R.id.displaySpotDismissBtn)).setOnClickListener(view1 -> {
                    bottomSheetDialog.dismiss();
                    mapView.getController().animateTo(
                            point,
                            previousZoomLevel,
                            750L);
                });

                bottomSheetDialog.setOnDismissListener(dialogInterface -> {
                    m.setIcon(getResources().getDrawable(R.drawable.map_location_circle));
                    map.invalidate();
                });

                return true;
            });
            markFeatures.put(marker, feature);

            map.getOverlays().add(marker);
        }
    }
}