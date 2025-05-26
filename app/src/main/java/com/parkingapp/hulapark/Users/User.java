package com.parkingapp.hulapark.Users;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.parkingapp.hulapark.DataModels.User.UserDataModel;
import com.parkingapp.hulapark.Utilities.DBManager;
import com.parkingapp.hulapark.Utilities.Users.UserFragDisplayConfigurator;

public class User extends AUser
{
    private String userUID;
    private DatabaseReference userDataModelReference;
    private UserDataModel userDataModel;

    public User(String uid, DataSnapshot dataSnapshot)
    {
        this.userUID = uid;
        this.userDataModel = DBManager.parseUser(dataSnapshot);
        this.userDataModelReference = dataSnapshot.getRef();

        userDataModelReference.addListenerForSingleValueEvent(new ValueEventListener()
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
        });
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
}
