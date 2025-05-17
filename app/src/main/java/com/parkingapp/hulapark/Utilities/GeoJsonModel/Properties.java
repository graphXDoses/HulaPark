package com.parkingapp.hulapark.Utilities.GeoJsonModel;

import com.google.gson.annotations.SerializedName;

public class Properties
{
    @SerializedName("OBJECTID")
    public int objectId;

    @SerializedName("ID")
    public String id;

    @SerializedName("NAME")
    public String name;

    @SerializedName("LOCATION")
    public String location;

    @SerializedName("LANDMARK")
    public String landmark;

    @SerializedName("ADDRESS")
    public String address;

    @SerializedName("ENTRY")
    public String entry;

    @SerializedName("EXIT_")
    public String exit;

    @SerializedName("PARKING_TY")
    public String parkingType;

    @SerializedName("SPACES")
    public int spaces;

    @SerializedName("HANDICAP_S")
    public int handicapSpaces;

    @SerializedName("MANAGED_BY")
    public String managedBy;

    @SerializedName("OWN")
    public String owner;
}
