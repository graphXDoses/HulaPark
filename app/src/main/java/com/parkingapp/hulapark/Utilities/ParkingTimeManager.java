package com.parkingapp.hulapark.Utilities;

public interface ParkingTimeManager
{
    void onTickUpdate(int remaining);
    boolean onFinish();
//    Runnable onFinish = null;
}
