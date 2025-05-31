package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards;

import android.os.Handler;
import android.os.Looper;

import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingTimeManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BalanceIncCardDataModel extends ActionCardDataModel
{
    public BalanceIncCardDataModel(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }
    @Override
    public int getType()
    {
        return 1;
    }

    public String getTimestampString()
    {
        return this.startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
