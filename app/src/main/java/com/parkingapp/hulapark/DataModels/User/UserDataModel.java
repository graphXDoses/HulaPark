package com.parkingapp.hulapark.DataModels.User;

import java.util.Map;

public class UserDataModel
{
    public Map<String, ActionLogsDataModel> ActionLogs;
    public WalletDataModel Wallet;
    public boolean isAdmin;

    public UserDataModel() {} // Required for Firebase
}
