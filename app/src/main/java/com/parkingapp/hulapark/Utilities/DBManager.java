package com.parkingapp.hulapark.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parkingapp.hulapark.DataModels.User.ActionLogsDataModel;
import com.parkingapp.hulapark.DataModels.User.BalanceIncLogDataModel;
import com.parkingapp.hulapark.DataModels.User.ParkingLogDataModel;
import com.parkingapp.hulapark.DataModels.User.UserDataModel;
import com.parkingapp.hulapark.DataModels.User.WalletDataModel;
import com.parkingapp.hulapark.Users.User;

public class DBManager
{
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://hulapark-34841-default-rtdb.europe-west1.firebasedatabase.app/");
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static ActionLogsDataModel parseActionLog(DataSnapshot snapshot)
    {
        String type = snapshot.child("Type").getValue(String.class);
        if ("PARKING".equals(type)) {
            return snapshot.getValue(ParkingLogDataModel.class);
        } else if ("BALANCE_INC".equals(type)) {
            return snapshot.getValue(BalanceIncLogDataModel.class);
        } else {
            return snapshot.getValue(ActionLogsDataModel.class);
        }
    }

    public static DatabaseReference getRef(String refString)
    {
        return db.getReference(refString);
    }

    public static void authenticateUserCredentials(String email, String password, OnCompleteListener<AuthResult> listener)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public static void createUserDataModel(String uid)
    {
        DatabaseReference userRef = getRef("Users/" + uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                UserDataModel dataModel = snapshot.getValue(UserDataModel.class);
                if (dataModel != null)
                    User.setUserDataModel(dataModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

    public static FirebaseUser getCurrentUserAuthSertificate()
    {
        return mAuth.getCurrentUser();
    }

    public static WalletDataModel parseWallet(DataSnapshot snapshot)
    {
        return snapshot.getValue(WalletDataModel.class);
    }
}
