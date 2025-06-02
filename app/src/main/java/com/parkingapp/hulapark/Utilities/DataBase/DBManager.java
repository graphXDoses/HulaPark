package com.parkingapp.hulapark.Utilities.DataBase;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.Admin;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.BalanceIncCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.BalanceIncLogDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User.NewBalanceIncLogDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User.NewParkingLogDataModel;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class DBManager
{
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance("https://hulapark-34841-default-rtdb.europe-west1.firebasedatabase.app/");
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static DatabaseReference getRef(String refString)
    {
        return db.getReference(refString);
    }

    private static final AuthPipelineData pipeline = new AuthPipelineData();

    public static void createNewUserFromCredentials(String email, String password, Consumer<Exception> onCompleteFunc)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .continueWithTask(task ->
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    pipeline.uid = getCurrentUserAuthSertificate().getUid();
                    return getRef("Users/" + pipeline.uid).get();
                }).continueWithTask(userDataSnapshot ->
                {
                    pipeline.userDataSnapshot = userDataSnapshot.getResult();
                    return getRef("Users/" + pipeline.uid).child("Wallet").child("Balance").setValue(0);
                }).continueWithTask(task ->
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return getRef("CatholicData").get();
                }).addOnSuccessListener(catholicDataSnapshot ->
                {
                    CommonFragUtils.FragmentSwapper.changeUserTo(new User(pipeline.userDataSnapshot, catholicDataSnapshot));
                    onCompleteFunc.accept(null);
                }).addOnFailureListener(e ->
                {
                    onCompleteFunc.accept(e);
                });
    }

    public static void authenticateUserCredentials(String email, String password, Consumer<Exception> onCompleteFunc)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .continueWithTask(task ->
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    pipeline.uid = getCurrentUserAuthSertificate().getUid();
                    DatabaseReference adminUser = getRef("Admins").child(pipeline.uid);
                    return adminUser.get();
                }).continueWithTask(adminsDataSnapshot ->
                {
                    pipeline.isAdmin = adminsDataSnapshot.getResult().exists();
                    return getRef("CatholicData").get();
                }).continueWithTask(catholicDataSnapshot ->
                {
                    pipeline.catholicDataSnapshot = catholicDataSnapshot.getResult();
                    if (pipeline.isAdmin)
                        return Tasks.forResult(null);
                    return getRef("Users/" + pipeline.uid).get();
                }).addOnSuccessListener(userDataSnapshot ->
                {
                    if (pipeline.isAdmin)
                        CommonFragUtils.FragmentSwapper.changeUserTo(new Admin(pipeline.catholicDataSnapshot));
                    else
                        CommonFragUtils.FragmentSwapper.changeUserTo(new User(userDataSnapshot, pipeline.catholicDataSnapshot));
                    onCompleteFunc.accept(null);
                }).addOnFailureListener(e ->
                {
                    onCompleteFunc.accept(e);
                });
    }

    public static FirebaseUser getCurrentUserAuthSertificate()
    {
        return mAuth.getCurrentUser();
    }

    public static void setNewParking(User user, long startTime, double newBalance, NewParkingLogDataModel dataModel)
    {
        String uid = getCurrentUserAuthSertificate().getUid();
        DatabaseReference actionLogRef = user.getRef()
                .child("ActionLogs")
                .child(startTime + "");
        DatabaseReference balanceRef = user.getRef().child("Wallet").child("Balance");

        actionLogRef.setValue(dataModel).addOnSuccessListener(aVoid ->
                {
                    Log.d("Firebase", "Action log added successfully");
                })
                .addOnFailureListener(e ->
                {
                    Log.e("Firebase", "Failed to add action log", e);
                });
        balanceRef.setValue(newBalance);
    }

    public static void updateBalance(User user, long timestamp, double balance, NewBalanceIncLogDataModel dataModel)
    {
        DatabaseReference balanceRef = user.getRef().child("Wallet").child("Balance");
        DatabaseReference actionLogRef = user.getRef().child("ActionLogs").child(timestamp + "");

        balanceRef.setValue(balance);
        actionLogRef.setValue(dataModel);
    }

    public static void logout()
    {
        mAuth.signOut();
        CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
    }

    public static Task<Void> deleteCurrentUsersAccount()
    {
        return mAuth.getCurrentUser().delete();
    }

    public static CurrentUserCreds getCurrentUserCreds()
    {
        LocalDateTime createT = LocalDateTime.ofInstant(Instant.ofEpochMilli(mAuth.getCurrentUser().getMetadata().getCreationTimestamp()), ZoneId.systemDefault());
        LocalDateTime lastVisT = LocalDateTime.ofInstant(Instant.ofEpochMilli(mAuth.getCurrentUser().getMetadata().getLastSignInTimestamp()), ZoneId.systemDefault());

        CurrentUserCreds creds = new CurrentUserCreds();
        creds.setEmail(mAuth.getCurrentUser().getEmail());
        creds.setCreatedDate(createT.format(DateTimeFormatter.ofPattern("dd/MM/Y - HH:mm")));
        creds.setLastVisited(lastVisT.format(DateTimeFormatter.ofPattern("dd/MM/Y - HH:mm")));
        return creds;
    }

    public static String getGreekMessageForErrorCode(Exception e)
    {
        String errorCode;

        if (e instanceof FirebaseAuthInvalidUserException) {
            errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            errorCode = ((FirebaseAuthInvalidCredentialsException) e).getErrorCode();
        } else if (e instanceof FirebaseAuthException) {
            errorCode = ((FirebaseAuthException) e).getErrorCode();
        } else {
            errorCode = "Άγνωστο Σφάλμα";
        }

        switch (errorCode)
        {
            case "ERROR_INVALID_EMAIL":
                return "Μη έγκυρη διεύθυνση email.";
            case "ERROR_WRONG_PASSWORD":
                return "Λάθος κωδικός.";
            case "ERROR_USER_NOT_FOUND":
                return "Ο χρήστης δεν βρέθηκε.";
            case "ERROR_USER_DISABLED":
                return "Ο λογαριασμός έχει απενεργοποιηθεί.";
            case "ERROR_EMAIL_ALREADY_IN_USE":
                return "Αυτή η διεύθυνση email χρησιμοποιείται ήδη.";
            case "ERROR_WEAK_PASSWORD":
                return "Ο κωδικός είναι πολύ αδύναμος.";
            case "ERROR_TOO_MANY_REQUESTS":
                return "Πάρα πολλές προσπάθειες. Δοκιμάστε ξανά αργότερα.";
            default:
                return "Παρουσιάστηκε άγνωστο σφάλμα.";
        }
    }

    private static class AuthPipelineData
    {
        public String uid;
        public Boolean isAdmin;
        public DataSnapshot catholicDataSnapshot;
        public DataSnapshot userDataSnapshot;
    }
}