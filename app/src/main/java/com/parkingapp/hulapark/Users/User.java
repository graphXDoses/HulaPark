package com.parkingapp.hulapark.Users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.FeatureCollection;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.GeoJsonDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ActionCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.BalanceIncCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.BalanceIncLogDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.ParkingLogDataModel;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.UserFragDisplayConfigurator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class User
{
    private DatabaseReference catholicDataSnapshotReference;
    private DatabaseReference userDataSnapshotReference;
    // ActionLog
    private ArrayList<ParkingLogDataModel> parkingLogDataModels;
    private ArrayList<BalanceIncLogDataModel> balanceIncLogDataModels;
    // Wallet
    private MutableLiveData<Double> Balance = new MutableLiveData<>();

    private MutableLiveData<Double> bphPrice = new MutableLiveData<>();

    public User(DataSnapshot userDataSnapshot, DataSnapshot catholicDataSnapshot)
    {
        this.userDataSnapshotReference = userDataSnapshot.getRef();
        this.catholicDataSnapshotReference = catholicDataSnapshot.getRef();

        userDataSnapshotReference.addListenerForSingleValueEvent(new UserDataSnapshotChangeListener());
        catholicDataSnapshotReference.addListenerForSingleValueEvent(new CatholicDataSnapshotChangeListener());
    }

    public DatabaseReference getRef()
    {
        return userDataSnapshotReference;
    }

    private void refreshCatholicData(DataSnapshot catholicDataSnapshot)
    {
        DataSnapshot geoDataSnapshot = catholicDataSnapshot.child("GeoData");

        FeatureCollection collection = geoDataSnapshot.getValue(FeatureCollection.class);

        GeoJsonDataModel geoJsonDataModel = new GeoJsonDataModel();
        geoJsonDataModel.GeoData = collection;
        CommonFragUtils.FragmentSwapper.setGeoDataModel(geoJsonDataModel);

        DataSnapshot bphPriceSnapshot = catholicDataSnapshot.child("BasePerHourPrice");
        bphPrice.setValue(bphPriceSnapshot.getValue(Double.class));
    }

    private void refreshUserData(DataSnapshot userDataSnapshot)
    {
        DataSnapshot logsSnapshot = userDataSnapshot.child("ActionLogs");
        parkingLogDataModels = new ArrayList<>();
        balanceIncLogDataModels = new ArrayList<>();
        for (DataSnapshot logSnapshot : logsSnapshot.getChildren()) {
            String type = logSnapshot.child("Type").getValue(String.class);
            long timestamp = Long.parseLong(logSnapshot.getKey());
            switch (type) {
                case "PARKING":
                    ParkingLogDataModel parkingLog = logSnapshot.getValue(ParkingLogDataModel.class);
                    parkingLog.setStartTime(timestamp);
                    parkingLogDataModels.add(parkingLog);
                    break;
                case "BALANCE_INC":
                    BalanceIncLogDataModel balanceIncLog = logSnapshot.getValue(BalanceIncLogDataModel.class);
                    balanceIncLog.setTimeStamp(timestamp);
                    balanceIncLogDataModels.add(balanceIncLog);
                    break;
                default:
                    Log.w("ActionLogs", "Unknown log type: " + type);
            }
        }
        DataSnapshot walletSnapshot = userDataSnapshot.child("Wallet");
        Double balance = walletSnapshot.child("Balance").getValue(Double.class);
        Balance.postValue(balance);

        ArrayList<ActionCardDataModel> historyCards = new ArrayList<>();
        for (BalanceIncLogDataModel model : balanceIncLogDataModels)
        {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(model.getTimeStamp()), ZoneId.systemDefault());
            BalanceIncCardDataModel cardModel = new BalanceIncCardDataModel(start);
            cardModel.setAmount(String.format("%.2f", model.Amount));

            historyCards.add(cardModel);
        }

        for (ParkingLogDataModel model : parkingLogDataModels)
        {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(model.getStartTime()), ZoneId.systemDefault());
            LocalDateTime finish = LocalDateTime.ofInstant(Instant.ofEpochMilli(model.FinishTime), ZoneId.systemDefault());
            ParkingCardDataModel cardModel = new ParkingCardDataModel(start, finish);
            cardModel.setPlateNumber(model.VehicleID)
                    .setLocationID(model.SectorID)
                    .setAmount(String.format("%.2f", model.Price));

            historyCards.add(cardModel);
        }

        historyCards = (ArrayList<ActionCardDataModel>)historyCards.stream().sorted().collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        ArrayList<ActionCardDataModel> history = new ArrayList<>(historyCards.stream()
                .filter(card ->
                {
                    if(card instanceof ParkingCardDataModel)
                        return  ((ParkingCardDataModel)card).getFinishTime().isBefore(LocalDateTime.now());
                    return true;
                }).collect(Collectors.toList()));

        ArrayList<ParkingCardDataModel> ongoing = new ArrayList<>();
        for (ActionCardDataModel model : historyCards)
        {
            if(model instanceof ParkingCardDataModel)
            {
                if(((ParkingCardDataModel)model).getStartTime().isBefore(now) && ((ParkingCardDataModel)model).getFinishTime().isAfter(now))
                    ongoing.add((ParkingCardDataModel) model);
            }
        }

        CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter().setCards(history);
        CommonFragUtils.FragmentSwapper.getParkingCardAdapter().setCards(ongoing);
    }

    public static void setFragmentContainerActiveFrag(int container, int frag)
    {
        UserFragDisplayConfigurator.setFragmentContainerActiveFrag(User.class, container, frag);
    }

    public static int getFragmentContainerActiveFrag(int container)
    {
        return UserFragDisplayConfigurator.getFragmentContainerActiveFrag(User.class, container);
    }

    public MutableLiveData<Double> getBalance()
    {
        return Balance;
    }
    public MutableLiveData<Double> getBPHPrice()
    {
        return bphPrice;
    }

    class UserDataSnapshotChangeListener implements ValueEventListener
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            Log.i("DATA_REFRESH", "Refreshing user data.");
            refreshUserData(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {
            Log.e("Firebase", "Error: " + error.getMessage());
        }
    }

    class CatholicDataSnapshotChangeListener implements ValueEventListener
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            Log.i("DATA_REFRESH", "Refreshing catholic data.");
            refreshCatholicData(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {
            Log.e("Firebase", "Error: " + error.getMessage());
        }
    }
}