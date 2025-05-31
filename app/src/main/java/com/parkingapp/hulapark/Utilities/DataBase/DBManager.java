package com.parkingapp.hulapark.Utilities.DataBase;

import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parkingapp.hulapark.Users.Admin;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User.NewParkingLogDataModel;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;

import java.util.function.Consumer;

public class DBManager
{
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://hulapark-34841-default-rtdb.europe-west1.firebasedatabase.app/");
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static DatabaseReference getRef(String refString)
    {
        return db.getReference(refString);
    }

    public static void authenticateUserCredentials(String email, String password, Consumer<Exception> onCompleteFunc)
    {
        mAuth.signInWithEmailAndPassword(email, password)
        .continueWithTask(task ->
        {
            if (!task.isSuccessful()) { throw task.getException(); }
            AuthPipelineData.uid = getCurrentUserAuthSertificate().getUid();
            DatabaseReference adminUser = getRef("Admins").child(AuthPipelineData.uid);
            return adminUser.get();
        }).continueWithTask(adminsDataSnapshot -> {
            AuthPipelineData.isAdmin = adminsDataSnapshot.getResult().exists();
            return getRef("Data").get();
        }).continueWithTask(catholicDataSnapshot -> {
            AuthPipelineData.catholicDataSnapshot = catholicDataSnapshot.getResult();
            if(AuthPipelineData.isAdmin)
                return Tasks.forResult(null);
            return getRef("Users/" + AuthPipelineData.uid).get();
        }).addOnSuccessListener(userDataSnapshot ->
        {
            if(AuthPipelineData.isAdmin)
                CommonFragUtils.FragmentSwapper.changeUserTo(new Admin(AuthPipelineData.catholicDataSnapshot));
            else
                CommonFragUtils.FragmentSwapper.changeUserTo(new User(userDataSnapshot, AuthPipelineData.catholicDataSnapshot));
            onCompleteFunc.accept(null);
        }).addOnFailureListener(e -> { onCompleteFunc.accept(e); });
    }

    public static FirebaseUser getCurrentUserAuthSertificate()
    {
        return mAuth.getCurrentUser();
    }

    public static void setNewParking(long startTime, NewParkingLogDataModel dataModel)
    {
        String uid = getCurrentUserAuthSertificate().getUid();
        DatabaseReference actionLogRef = db
                .getReference("Users")
                .child(uid)
                .child("ActionLogs")
                .child(startTime + "");

        actionLogRef.setValue(dataModel).addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Action log added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to add action log", e);
                });
    }

    public static void logout()
    {
        mAuth.signOut();
        CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
    }
}

class AuthPipelineData
{
    protected static String uid;
    protected static Boolean isAdmin;
    protected static DataSnapshot catholicDataSnapshot;
}
