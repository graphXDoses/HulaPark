package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ActionCardDataModel
{
    public abstract int getType();

    protected LocalDateTime startTime;
    protected String amount = "";

    public String getAmount()
    {
        return amount;
    }

    public ActionCardDataModel setAmount(String amount)
    {
        this.amount = amount;
        return this;
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    public String getDate()
    {
        return this.startTime.format(DateTimeFormatter.ofPattern("d/M/Y"));
    }

    public String getStartHourString()
    {
        return this.startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
