package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User;

import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.ActionLogsDataModel;

public class NewBalanceIncLogDataModel extends ActionLogsDataModel
{
    public double Amount;

    public NewBalanceIncLogDataModel() { Type = "BALANCE_INC"; }
}
