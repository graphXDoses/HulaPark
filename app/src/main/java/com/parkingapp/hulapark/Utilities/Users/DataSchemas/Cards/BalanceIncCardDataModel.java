package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards;

import android.os.Handler;
import android.os.Looper;

import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingTimeManager;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.BalanceIncLogDataModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    @Override
    public int compareTo(ActionCardDataModel o)
    {
        if (o instanceof ParkingCardDataModel)
        {
            ParkingCardDataModel p = (ParkingCardDataModel) o;
            int i = getStartTime().isAfter(p.getFinishTime()) ? 1 : -1;
            return i;
        }
        if(o instanceof BalanceIncCardDataModel)
        {
            BalanceIncCardDataModel p = (BalanceIncCardDataModel) o;
            int i = getStartTime().isAfter(p.getStartTime()) ? 1 : -1;
            return i;
        }
        return 0;
    }
}
