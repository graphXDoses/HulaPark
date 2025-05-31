package com.parkingapp.hulapark.Users;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.parkingapp.hulapark.Utilities.Users.InteractsWithDB;

public class Admin
{
    private DatabaseReference catholicDataModelReference;


    public Admin(DataSnapshot catholicDataSnapshot)
    {
        this.catholicDataModelReference = catholicDataSnapshot.getRef();
        catholicDataModelReference.addListenerForSingleValueEvent(new CatholicDataModelChangeListener());
    }

    public void refreshData(DataSnapshot catholicDataSnapshot)
    {

    }

    class CatholicDataModelChangeListener implements ValueEventListener
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            // Needs work!
            Log.i("DATA_REFRESH", "Refreshing user data.");
            refreshData(snapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {
            Log.e("Firebase", "Error: " + error.getMessage());
        }
    }
}

