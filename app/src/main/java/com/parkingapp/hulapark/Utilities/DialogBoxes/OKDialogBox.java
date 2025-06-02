package com.parkingapp.hulapark.Utilities.DialogBoxes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.parkingapp.hulapark.Adapters.ParkedVehiclesAdapter;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;

import java.util.ArrayList;
import java.util.function.Function;

public class OKDialogBox extends Dialog
{
    public static void setBackground(Drawable background)
    {
        OKDialogBox.background = background;
    }

    public static Drawable background;
    private RecyclerView rcv;

    public OKDialogBox(Context context)
    {
        super(context);
    }

    @SuppressLint("MissingInflatedId")
    public OKDialogBox builder()
    {
        setCancelable(false);
        setContentView(R.layout.ok_dialog_box);
        getWindow().setBackgroundDrawable(background);
        findViewById(R.id.OKDB_OK).setOnClickListener(view -> {
            dismiss();
        });
        return this;
    }

    public OKDialogBox setHeader(String headerText)
    {
        ((TextView)findViewById(R.id.ok_db_header)).setText(headerText);
        return this;
    }

    public OKDialogBox setBodyAndInflate(int resID, Function<FrameLayout, RecyclerView> afterInflation)
    {
        FrameLayout container = findViewById(R.id.ok_db_body);
        getLayoutInflater().from(getContext()).inflate(resID, container, true);
        rcv = afterInflation.apply(container);
        return this;
    }

    public OKDialogBox passTheParkings(ArrayList<ParkingCardDataModel> parkings)
    {
        ((ParkedVehiclesAdapter)rcv.getAdapter()).setParkings(parkings);
        return this;
    }
}
