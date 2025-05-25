package com.parkingapp.hulapark.Users;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.parkingapp.hulapark.DataModels.User.ActionLogsDataModel;
import com.parkingapp.hulapark.DataModels.User.UserDataModel;
import com.parkingapp.hulapark.DataModels.User.WalletDataModel;
import com.parkingapp.hulapark.Utilities.DBManager;
import com.parkingapp.hulapark.Utilities.Users.UserFragDisplayConfigurator;

public class User
{
    private static UserDataModel dataModel;
    private static final String UsersPrefix = "Users/";
    private static ActionLogsDataModel actionLogDataModel;
    private static WalletDataModel walletDataModel;

    public static ActionLogsDataModel getActionLogsDataModel()
    {
        return actionLogDataModel;
    }

    public static void setActionLogDataModel(ActionLogsDataModel actionLogDataModel)
    {
        User.actionLogDataModel = actionLogDataModel;
    }


    public static void onActionLogsResponse(OnCompleteListener<DataSnapshot> listener)
    {
        DBManager.getRef("Users/wtEzDQXaznX7vg5aNJPgTankGti1/ActionLogs/1748179801209").get().addOnCompleteListener(listener);
    }

    public static void onWalletResponse(OnCompleteListener<DataSnapshot> listener)
    {
        DBManager.getRef("Users/wtEzDQXaznX7vg5aNJPgTankGti1/Wallet").get().addOnCompleteListener(listener);
    }
    public static void setFragmentContainerActiveFrag(int container, int frag)
    {
        UserFragDisplayConfigurator.setFragmentContainerActiveFrag(User.class, container, frag);
    }

    public static int getFragmentContainerActiveFrag(int container)
    {
        return UserFragDisplayConfigurator.getFragmentContainerActiveFrag(User.class, container);
    }

    public static void setUserDataModel(UserDataModel dataModel)
    {
        dataModel = dataModel;
    }

    public static UserDataModel getUserDataModel()
    {
        return dataModel;
    }

    public static WalletDataModel getWalletDataModel()
    {
        return walletDataModel;
    }

    public static void setWalletDataModel(WalletDataModel walletDataModel)
    {
        User.walletDataModel = walletDataModel;
    }
}
