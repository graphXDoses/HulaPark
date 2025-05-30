package com.parkingapp.hulapark.Users.DataModels.User;

import java.util.Map;

public class UserDataModel
{
    public transient Map<String, ActionLogsDataModel> ActionLogs;
    public WalletDataModel Wallet;

    public UserDataModel() {} // Required for Firebase
}
