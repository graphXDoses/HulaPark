package com.parkingapp.hulapark.Users.DataModels.User;

public class NewParkingLogDataModel  extends ActionLogsDataModel
{
    public long FinishTime;
    public double Price;
    public String SectorID;
    public String VehicleID;

    public NewParkingLogDataModel() { Type = "PARKING"; }
}
