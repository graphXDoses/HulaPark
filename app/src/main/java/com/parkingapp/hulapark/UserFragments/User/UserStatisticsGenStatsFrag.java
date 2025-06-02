package com.parkingapp.hulapark.UserFragments.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.parkingapp.hulapark.Adapters.HistoryParkingCardAdapter;
import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.DataBase.CurrentUserCreds;
import com.parkingapp.hulapark.Utilities.DataBase.DBManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ActionCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.BalanceIncCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserStatisticsGenStatsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserStatisticsGenStatsFrag extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int ongoingParkings = 0;
    private int doneParkings = 0;
    private OngoingParkingCardAdapter parkingCardAdapter;
    private HistoryParkingCardAdapter historyParkingCardAdapter;
    private int balanceIncs;

    public UserStatisticsGenStatsFrag()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserStatisticsGenStatsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserStatisticsGenStatsFrag newInstance(String param1, String param2)
    {
        UserStatisticsGenStatsFrag fragment = new UserStatisticsGenStatsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_user_statistics_gen_stats, container, false);
        // Account
        TextView genStatsUserEmail = view.findViewById(R.id.genStatsUserEmail);
        TextView genStatsUserCreated = view.findViewById(R.id.genStatsUserCreated);
        TextView genStatsUserLastVisited = view.findViewById(R.id.genStatsUserLastVisited);
        MaterialButton genStatsUserDelAccount = view.findViewById(R.id.genStatsUserDelAccount);
        // Expenses
        TextView genStatsUserTotalCost = view.findViewById(R.id.genStatsUserTotalCost);
        TextView genStatsUserTotalCredit = view.findViewById(R.id.genStatsUserTotalCredit);
        TextView genStatsUserParkNum = view.findViewById(R.id.genStatsUserParkNum);
        // Favs
        TextView genStatsUserFavParking = view.findViewById(R.id.genStatsUserFavParking);
        TextView genStatsUserFavVehicle = view.findViewById(R.id.genStatsUserFavVehicle);
        User user = (User) (CommonFragUtils.FragmentSwapper.getUser());

        CurrentUserCreds creds = DBManager.getCurrentUserCreds();

        genStatsUserEmail.setText(creds.getEmail());
        genStatsUserCreated.setText(creds.getCreatedDate());
        genStatsUserLastVisited.setText(creds.getLastVisited());

        parkingCardAdapter = CommonFragUtils.FragmentSwapper.getParkingCardAdapter();
        historyParkingCardAdapter = CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter();

        parkingCardAdapter.getLiveData().observe(getActivity(), ongoingParkings ->
        {
            List<ActionCardDataModel> allHistory = historyParkingCardAdapter.getAllHistory();
            this.ongoingParkings = ongoingParkings.size();

            List<BalanceIncCardDataModel> balanceIncs = allHistory.stream().filter(e -> e.getType() == 1)
                                        .map(e -> (BalanceIncCardDataModel)e).collect(Collectors.toList());
            List<ParkingCardDataModel> oldParkings = allHistory.stream().filter(e -> e.getType() == 0)
                    .map(e -> (ParkingCardDataModel)e).collect(Collectors.toList());
            doneParkings = historyParkingCardAdapter.getItemCount() - balanceIncs.size();
            List<ParkingCardDataModel> allParkings = Stream.concat(ongoingParkings.stream(), oldParkings.stream()).collect(Collectors.toList());

            genStatsUserParkNum.setText(String.valueOf(this.ongoingParkings + doneParkings));

            genStatsUserTotalCredit.setText((String.format("%.2f", balanceIncs.stream().map(e -> Double.parseDouble(e.getAmount())).reduce(0.0, Double::sum))));
            genStatsUserTotalCost.setText(String.format("%.2f", allParkings.stream().map(e -> Double.parseDouble(e.getAmount())).reduce(0.0, Double::sum)));

            String mostFrequentVehicle = getMostFrequentVehicle(allParkings);
            String mostFrequentParkingLoc = getMostFrequentParkingLoc(allParkings);

            genStatsUserFavVehicle.setText(mostFrequentVehicle != null ? mostFrequentVehicle : "---");
            genStatsUserFavParking.setText(mostFrequentParkingLoc != null ? mostFrequentParkingLoc : "---");
        });

        genStatsUserDelAccount.setOnClickListener(__ ->
        {
            DBManager.deleteCurrentUsersAccount().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
                    Toast.makeText(getContext(), "Account Deleted!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "An error has occured..", Toast.LENGTH_SHORT).show();
            });
        });

        return view;
    }

    private String getMostFrequentVehicle(List<ParkingCardDataModel> parkingList)
    {
        if(parkingList.isEmpty())
            return null;

        Map<String, Integer> freqMap = new HashMap<>();
        Map<String, ParkingCardDataModel> parkingMap = new HashMap<>();

        for (ParkingCardDataModel p : parkingList)
        {
            String id = p.getPlateNumber();
            freqMap.put(id, freqMap.getOrDefault(id, 0) + 1);
            parkingMap.putIfAbsent(id, p);
        }

        int maxFreq = Collections.max(freqMap.values());
        long count = freqMap.values().stream().filter(freq -> freq == maxFreq).count();

        if (count > 1) { return null; }

        for (Map.Entry<String, Integer> entry : freqMap.entrySet())
        {
            if (entry.getValue() == maxFreq)
            {
                return parkingMap.get(entry.getKey()).getPlateNumber();
            }
        }

        return null;
    }

    private String getMostFrequentParkingLoc(List<ParkingCardDataModel> parkingList)
    {
        if(parkingList.isEmpty())
            return null;

        Map<String, Integer> freqMap = new HashMap<>();
        Map<String, ParkingCardDataModel> parkingMap = new HashMap<>();

        for (ParkingCardDataModel p : parkingList)
        {
            String id = p.getSectorID();
            freqMap.put(id, freqMap.getOrDefault(id, 0) + 1);
            parkingMap.putIfAbsent(id, p);
        }

        int maxFreq = Collections.max(freqMap.values());
        long count = freqMap.values().stream().filter(freq -> freq == maxFreq).count();

        if (count > 1) { return null; }

        for (Map.Entry<String, Integer> entry : freqMap.entrySet())
        {
            if (entry.getValue() == maxFreq)
            {
                String sectorID = parkingMap.get(entry.getKey()).getSectorID();
                List<String> addressList = CommonFragUtils.FragmentSwapper.getGeoDataModel().GeoData.features
                        .stream().filter(f -> f.properties.SECTORID.equals(sectorID))
                        .map(f -> f.properties.ADDRESS)
                        .collect(Collectors.toList());
                if (addressList.size() == 1)
                    return addressList.get(0);
                return null;
            }
        }

        return null;
    }
}