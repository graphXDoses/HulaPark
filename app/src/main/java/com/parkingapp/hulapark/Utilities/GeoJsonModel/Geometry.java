package com.parkingapp.hulapark.Utilities.GeoJsonModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geometry
{
    @SerializedName("type")
    public String type;

    @SerializedName("coordinates")
    public List<Double> coordinates; // [longitude, latitude]
}
