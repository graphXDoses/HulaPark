package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User;

public class BalanceIncLogDataModel extends ActionLogsDataModel
{
    public double Amount;
    private long TimeStamp;

    public BalanceIncLogDataModel() { Type = "BALANCE_INC"; }

    public void setTimeStamp(long timestamp)
    {
        this.TimeStamp = timestamp;
    }
}
