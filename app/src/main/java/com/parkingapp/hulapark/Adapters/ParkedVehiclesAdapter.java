package com.parkingapp.hulapark.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingTimeManager;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;

import java.util.ArrayList;

public class ParkedVehiclesAdapter extends RecyclerView.Adapter<ParkedVehiclesAdapter.ViewHolder> {
    private MutableLiveData<ArrayList<ParkingCardDataModel>> items = new MutableLiveData<>();
    private Context context;

    public ParkedVehiclesAdapter()
    {
        this.items.setValue(new ArrayList<>());
    }

    public ParkedVehiclesAdapter(ArrayList<ParkingCardDataModel> items)
    {
        this.items.setValue(items);
    }

    public void setParkings(ArrayList<ParkingCardDataModel> parkings)
    {
        this.items.setValue(parkings);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rcvi_parked_vehicle, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ParkedVehiclesAdapter.ViewHolder holder, int position)
    {
        ParkingCardDataModel thisCard = items.getValue().get(position);

        holder.vehicleID.setText(thisCard.getPlateNumber());
    }

    @Override
    public int getItemCount()
    {
        return items.getValue().size();
    }

    public LiveData<ArrayList<ParkingCardDataModel>> getLiveData()
    {
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView vehicleID;
        public ViewHolder(View view)
        {
            super(view);
            vehicleID = view.findViewById(R.id.parked_vehicle_id);
        }

    }
}
