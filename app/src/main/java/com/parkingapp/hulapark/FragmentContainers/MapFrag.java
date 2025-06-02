package com.parkingapp.hulapark.FragmentContainers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.parkingapp.hulapark.Activities.InitParkingScreen;
import com.parkingapp.hulapark.Adapters.ParkedVehiclesAdapter;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.DialogBoxes.OKDialogBox;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;
import com.parkingapp.hulapark.Utilities.DialogBoxes.WarningDialogBox;
import com.parkingapp.hulapark.Utilities.Map.HulaMap;
import com.parkingapp.hulapark.Utilities.Map.HulaMapMarker;
import com.parkingapp.hulapark.Utilities.Map.HulaMapMarkerBehaviourModifier;
import com.parkingapp.hulapark.Utilities.Users.UserType;

import android.preference.PreferenceManager;
import android.widget.TextView;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
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
    private BottomSheetDialog popupGotoInitParking = null;

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

        CommonFragUtils.FragmentSwapper.getUserType().observe(getViewLifecycleOwner(), userType ->
        {
            switch (userType)
            {
                case GUEST:
                {
                    new WarningDialogBox(getContext()).builder()
                            .setDescription("Ενδεχόμενα μη έκγυρα δεδομένα. Παρακαλούμε οπως συνδεθείτε στον λογαριασμό σας, ή δημιουργήσετε νεο για να λάβετε τις σύγχρονες τοποθεσίες.")
                            .show();
                    break;
                }
            }
        });

        HulaMap hulaMap = new HulaMap(view.findViewById(R.id.streetMap));

        hulaMap
        .setMapFocalPoint(new GeoPoint(21.309884, -157.858140), 13.0)
        .loadMapMarkers(getLayoutInflater(), CommonFragUtils.FragmentSwapper.getMapFeatures(), new HulaMapMarkerBehaviourModifier()
        {
            @Override
            public void chooseLayout(AtomicInteger layoutToInflate, Feature f)
            {
                LifecycleOwner owner = getActivity();
                CommonFragUtils.FragmentSwapper.getParkingCardAdapter().getLiveData().observe(owner, parkingCardDataModels ->
                {
                    List<ParkingCardDataModel> card = parkingCardDataModels.stream().filter(c -> c.getSectorID().equals(f.properties.SECTORID)).collect(Collectors.toList());
                    if(!card.isEmpty())
                        layoutToInflate.set(R.layout.inc_display_spot_details_ongoing);
                });
            }

            @Override
            public void afterInflating(Feature f, int layoutResID, View dynamicView, View parent, BottomSheetDialog bottomSheetDialog)
            {
                MaterialButton gotoInitParkingButton = (MaterialButton)parent.findViewById(R.id.displayGotoInitParkingButton);
                if(CommonFragUtils.FragmentSwapper.getUserType().getValue() == UserType.USER)
                {
                    User user = (User) CommonFragUtils.FragmentSwapper.getUser();

                    ((TextView)parent.findViewById(R.id.displaySpotFee)).setText(String.format("%.2f", user.getBPHPrice().getValue()));
                    gotoInitParkingButton.setOnClickListener(__ -> {
                        Intent intent = new Intent(getActivity(), InitParkingScreen.class);
                        intent.putExtra("SELECTED_SECTOR", ((TextView) bottomSheetDialog.findViewById(R.id.displaySectorID)).getText().toString());
                        popupGotoInitParking = bottomSheetDialog;
                        startActivity(intent);
                    });
                } else {
                    gotoInitParkingButton.setVisibility(View.GONE);
                }
                if(layoutResID == R.layout.inc_display_spot_details_ongoing)
                {
                    CommonFragUtils.FragmentSwapper.getParkingCardAdapter().getLiveData().observe(getViewLifecycleOwner(), allParkings ->
                    {
                        ArrayList<String> vehicleIDs;
                        vehicleIDs = (ArrayList<String>) allParkings.stream()
                                .filter(p -> p.getSectorID().equals(f.properties.SECTORID))
                                .map(p -> p.getPlateNumber())
                                .collect(Collectors.toList());
                        ((TextView)dynamicView.findViewById(R.id.displayShowVehicles)).setText("ΟΧΗΜΑΤΑ (" + vehicleIDs.size() + ")");
                    });

                    ((MaterialButton)dynamicView.findViewById(R.id.displayShowVehicles)).setOnClickListener(__ ->
                    {
                        CommonFragUtils.FragmentSwapper.getParkingCardAdapter().getLiveData().observe(getViewLifecycleOwner(), allParkings ->
                        {
                            ArrayList<ParkingCardDataModel> parkings;
                            parkings = (ArrayList<ParkingCardDataModel>) allParkings.stream()
                                    .filter(p -> p.getSectorID().equals(f.properties.SECTORID))
                                    .collect(Collectors.toList());
                            new OKDialogBox(getContext()).builder()
                                    .setHeader("Οχήματα")
                                    .setBodyAndInflate(R.layout.inc_vehicle_rc, frameLayout ->
                                    {
                                        RecyclerView rcv = frameLayout.findViewById(R.id.vehicle_rc);
                                        rcv.setAdapter(new ParkedVehiclesAdapter(parkings));
                                        LinearLayoutManager ll = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                        rcv.setLayoutManager(ll);

                                        return rcv;
                                    })
                                    .show();
                        });
                    });
                }
            }
        })
        .invalidateMap();

        view.findViewById(R.id.mapCenterFocalPoint).setOnClickListener(v ->
        {
            hulaMap.flyToMapFocalPoint(new GeoPoint(21.309884, -157.858140), 13.0);
        });

        gotoSector(hulaMap);

        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (popupGotoInitParking != null && popupGotoInitParking.isShowing()) {
            popupGotoInitParking.dismiss();
        }
    }

    private void gotoSector(HulaMap hulaMap)
    {
        String targetLocationString = CommonFragUtils.FragmentSwapper.getMapFocusedPoint();
        if(targetLocationString != null)
        {
            HulaMapMarker marker = HulaMapMarker.getMarkerBySectorID(targetLocationString);
            marker.getOnMarkerClickListener().onMarkerClick(marker, hulaMap.getMap());
        }
    }
}