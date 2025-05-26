package com.parkingapp.hulapark.Users;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.parkingapp.hulapark.DataModels.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.DataModels.User.ActionLogsDataModel;
import com.parkingapp.hulapark.DataModels.User.BalanceIncLogDataModel;
import com.parkingapp.hulapark.DataModels.User.ParkingLogDataModel;
import com.parkingapp.hulapark.DataModels.User.UserDataModel;
import com.parkingapp.hulapark.Utilities.DBManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.UserFragDisplayConfigurator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class User extends AUser
{
    private String userUID;
    private DatabaseReference userDataModelReference;
    private UserDataModel userDataModel;
    // ActionLog
    private ArrayList<ParkingLogDataModel> parkingLogDataModels = new ArrayList<>();
    private ArrayList<BalanceIncLogDataModel> balanceIncLogDataModels = new ArrayList<>();
    // Wallet
    private Double Balance = new Double(0.0f);


    public User(String uid, DataSnapshot userDataSnapshot)
    {
        this.userUID = uid;
        this.userDataModel = DBManager.parseUser(userDataSnapshot);
        this.userDataModelReference = userDataSnapshot.getRef();
        userDataModelReference.addListenerForSingleValueEvent(new UserDataModelChangeListener());

        // Parse userDataModel
        DataSnapshot logsSnapshot = userDataSnapshot.child("ActionLogs");
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

        ArrayList<ParkingCardDataModel> history = (ArrayList<ParkingCardDataModel>) parkingCards.stream()
        .filter(card -> card.getFinishTime().isBefore(LocalDateTime.now())).collect(Collectors.toList());

        CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter().setCards(history);
    }

    public void setUserDataModel(UserDataModel dataModel)
    {
        userDataModel = dataModel;
    }

    public UserDataModel getUserDataModel()
    {
        return userDataModel;
    }

    public String getUID()
    {
        return userUID;
    }
    public static void setFragmentContainerActiveFrag(int container, int frag)
    {
        UserFragDisplayConfigurator.setFragmentContainerActiveFrag(User.class, container, frag);
    }

    public static int getFragmentContainerActiveFrag(int container)
    {
        return UserFragDisplayConfigurator.getFragmentContainerActiveFrag(User.class, container);
    }
    class UserDataModelChangeListener implements ValueEventListener
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            // Needs work!
            UserDataModel dataModel = snapshot.getValue(UserDataModel.class);
            if (dataModel != null)
                userDataModel = dataModel;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {
            Log.e("Firebase", "Error: " + error.getMessage());
        }
    }
}

