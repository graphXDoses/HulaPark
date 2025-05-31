package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User;

import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.ActionLogsDataModel;

public class NewParkingLogDataModel  extends ActionLogsDataModel
{
    public long FinishTime;
    public double Price;
    public String SectorID;
    public String VehicleID;

    public NewParkingLogDataModel() { Type = "PARKING"; }
}
