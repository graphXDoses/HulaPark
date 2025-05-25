package com.parkingapp.hulapark.DataModels.User;

public class ParkingLogDataModel extends ActionLogsDataModel
{
    public long FinishTime;
    public double Price;
    public int SectorID;
    public String VehicleID;

    public ParkingLogDataModel() { Type = "PARKING"; }
}
