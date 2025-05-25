package com.parkingapp.hulapark.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parkingapp.hulapark.DataModels.ParkingCardModel;
import com.parkingapp.hulapark.R;

import java.util.ArrayList;

public class HistoryParkingCardAdapter extends RecyclerView.Adapter<HistoryParkingCardAdapter.ViewHolder> {
    private ArrayList<ParkingCardModel> items;
    private Context context;

    public HistoryParkingCardAdapter()
    {
        this.items = new ArrayList<>();
    }

    public HistoryParkingCardAdapter(ArrayList<ParkingCardModel> items)
    {
        this.items = items;
    }

    public void pushCard(ParkingCardModel cardModel)
    {
        items.add(cardModel);
        notifyItemInserted(getItemCount());
    }

    public void popCard(ParkingCardModel cardModel)
    {
        int position = items.indexOf(cardModel);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rcvi_parking_card_history, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryParkingCardAdapter.ViewHolder holder, int position)
    {
        ParkingCardModel thisCard = items.get(position);

        holder.plate_number.setText(thisCard.getPlateNumber());
        holder.location_id.setText(thisCard.getLocationID());
        holder.price.setText(thisCard.getPrice());
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView plate_number;
        TextView location_id;
        TextView price;

        public ViewHolder(View view)
        {
            super(view);
            plate_number = (view.findViewById(R.id.history_parkingcard_plate_number));
            location_id = (view.findViewById(R.id.history_parkingcard_sector_id));
            price = (view.findViewById(R.id.history_parkingcard_price));
        }

    }
}
