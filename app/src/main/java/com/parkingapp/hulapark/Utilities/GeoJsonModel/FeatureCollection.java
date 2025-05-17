package com.parkingapp.hulapark.Utilities.GeoJsonModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeatureCollection
{
    @SerializedName("type")
    public String type;

    @SerializedName("features")
    public List<Feature> features;
}
