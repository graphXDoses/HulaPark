package com.parkingapp.hulapark.Users;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.BalanceIncLogDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.ParkingLogDataModel;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.InteractsWithDB;
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

    private void refreshCatholicData(DataSnapshot catholicDataSnapshot)
    {
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

        ArrayList<ParkingCardDataModel> parkingCards = new ArrayList<>();
        for (ParkingLogDataModel model : parkingLogDataModels)
        {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(model.getStartTime()), ZoneId.systemDefault());
            LocalDateTime finish = LocalDateTime.ofInstant(Instant.ofEpochMilli(model.FinishTime), ZoneId.systemDefault());
            ParkingCardDataModel cardModel = new ParkingCardDataModel(start, finish);
            cardModel.setPlateNumber(model.VehicleID)
                    .setLocationID(model.SectorID)
                    .setPrice(String.format("%.2f", model.Price));

            parkingCards.add(cardModel);
        }

        LocalDateTime now = LocalDateTime.now();

        ArrayList<ParkingCardDataModel> history = (ArrayList<ParkingCardDataModel>) parkingCards.stream()
                .filter(card -> card.getFinishTime().isBefore(LocalDateTime.now())).collect(Collectors.toList());

        ArrayList<ParkingCardDataModel> ongoing = (ArrayList<ParkingCardDataModel>) parkingCards.stream()
                .filter(card ->
                        card.getStartTime().isBefore(now) && card.getFinishTime().isAfter(now)
                ).collect(Collectors.toList());

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

