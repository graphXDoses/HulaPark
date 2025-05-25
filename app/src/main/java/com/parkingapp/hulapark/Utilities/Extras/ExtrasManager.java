package com.parkingapp.hulapark.Utilities.Extras;

import android.content.Intent;
import android.os.Bundle;

import java.util.function.Consumer;

public class ExtrasManager
{
    private ExtrasManager() {}

    public static void getPassedExtras(Bundle savedInstanceState, Intent intent, Consumer<Bundle> f)
    {
        if (savedInstanceState == null)
        {
            Bundle extras = intent.getExtras();
            if(extras != null)
            {
                f.accept(extras);
            }
        }
    }
}
