package com.parkingapp.hulapark.Utilities.GeoJsonModel;

import com.google.gson.annotations.SerializedName;

public class Feature
{
    @SerializedName("type")
    public String type;

    @SerializedName("id")
    public int id;

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("properties")
    public Properties properties;
}
