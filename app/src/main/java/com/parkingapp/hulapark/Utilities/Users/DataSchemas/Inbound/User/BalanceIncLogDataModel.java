package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class BalanceIncLogDataModel extends ActionLogsDataModel
{
    public double Amount;
    private long TimeStamp;

    public BalanceIncLogDataModel() { Type = "BALANCE_INC"; }

    public void setTimeStamp(long timestamp)
    {
        this.TimeStamp = timestamp;
    }
    public long getTimeStamp()
    {
        return this.TimeStamp;
    }
}
