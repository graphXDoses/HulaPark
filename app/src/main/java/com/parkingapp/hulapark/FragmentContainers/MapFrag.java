package com.parkingapp.hulapark.FragmentContainers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.parkingapp.hulapark.Users.DataModels.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.DialogBoxes.WarningDialogBox;
import com.parkingapp.hulapark.Utilities.Map.HulaMap;
import com.parkingapp.hulapark.Utilities.Map.HulaMapMarker;
import com.parkingapp.hulapark.Utilities.Map.HulaMapMarkerBehaviourModifier;

import android.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    public void onDestroyView()
    {
        super.onDestroyView();
        CommonFragUtils.FragmentSwapper.setMapFocusedPoint(null);
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

        HulaMap hulaMap = new HulaMap(view.findViewById(R.id.streetMap));

        hulaMap
        .setMapFocalPoint(new GeoPoint(21.309884, -157.858140), 13.0)
        .loadMapMarkers(getLayoutInflater(), new HulaMapMarkerBehaviourModifier()
        {
            @Override
            public void chooseLayout(AtomicInteger layoutToInflate, Feature f)
            {
                LifecycleOwner owner = getActivity();
                CommonFragUtils.FragmentSwapper.getParkingCardAdapter().getLiveData().observe(owner, parkingCardDataModels -> {
                    List<ParkingCardDataModel> card = parkingCardDataModels.stream().filter(c -> c.getSectorID().equals(f.properties.sectorID)).collect(Collectors.toList());
                    if(card.isEmpty())
                        layoutToInflate.set(R.layout.inc_display_spot_details_select);
                    else
                        layoutToInflate.set(R.layout.inc_display_spot_details_ongoing);
                });
            }

            @Override
            public void afterInflating(int layoutResID, View dynamicView, BottomSheetDialog bottomSheetDialog)
            {
                if(layoutResID == R.layout.inc_display_spot_details_select)
                {
                    ((MaterialButton)dynamicView.findViewById(R.id.displaySpotDismissBtn)).setOnClickListener(__ -> {
                        bottomSheetDialog.dismiss();
                    });
                }
            }
        })
        .invalidateMap();

        String targetLocationString = CommonFragUtils.FragmentSwapper.getMapFocusedPoint();
        if(targetLocationString != null)
        {
            HulaMapMarker marker = HulaMapMarker.getMarkerBySectorID(targetLocationString);
            marker.getOnMarkerClickListener().onMarkerClick(marker, hulaMap.getMap());
        }

        return view;
    }
}