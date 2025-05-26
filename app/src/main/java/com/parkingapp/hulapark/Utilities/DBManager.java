package com.parkingapp.hulapark.Utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parkingapp.hulapark.DataModels.User.UserDataModel;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;

import java.util.function.Consumer;

public class DBManager
{
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://hulapark-34841-default-rtdb.europe-west1.firebasedatabase.app/");
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static UserDataModel parseUser(DataSnapshot snapshot)
    {
        return snapshot.getValue(UserDataModel.class);
    }

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
            String uid = getCurrentUserAuthSertificate().getUid();
            AuthPipelineData.dataReference = getRef("Users/" + uid);

            return AuthPipelineData.dataReference.get();
        }).addOnSuccessListener(result ->
        {
            AuthPipelineData.dataSnapshot = result;
            String uid = getCurrentUserAuthSertificate().getUid();
            CommonFragUtils.FragmentSwapper.changeUserTo(new User(uid, AuthPipelineData.dataSnapshot));
            onCompleteFunc.accept(null);
        }).addOnFailureListener(e -> { onCompleteFunc.accept(e); });
    }

    public static FirebaseUser getCurrentUserAuthSertificate()
    {
        return mAuth.getCurrentUser();
    }
}

class AuthPipelineData
{
    protected static DatabaseReference dataReference;
    protected static DataSnapshot dataSnapshot;
}
