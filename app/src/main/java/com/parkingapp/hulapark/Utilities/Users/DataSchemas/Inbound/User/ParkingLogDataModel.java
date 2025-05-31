package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User;

public class ParkingLogDataModel extends ActionLogsDataModel
{
    private long StartTime;
    public long FinishTime;
    public double Price;
    public String SectorID;
    public String VehicleID;

    public ParkingLogDataModel() { Type = "PARKING"; }

    public void setStartTime(Long StartTime)
    {
        this.StartTime = StartTime;
    }
    public long getStartTime()
    {
        return this.StartTime;
    }
}
