package com.parkingapp.hulapark.Utilities.ParkingCards;

public interface ParkingTimeManager
{
    void onTickUpdate(int remaining);
    boolean onFinish();
//    Runnable onFinish = null;
}
