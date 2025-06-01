package com.parkingapp.hulapark.Utilities.Map;

import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parkingapp.hulapark.Utilities.GeoJsonModel.Feature;

import java.util.concurrent.atomic.AtomicInteger;

public interface HulaMapMarkerBehaviourModifier
{
    void chooseLayout(AtomicInteger layoutToInflate, Feature feature);
    void afterInflating(int layoutResID, View view, View dynamicView, BottomSheetDialog bottomSheetDialog);
}
