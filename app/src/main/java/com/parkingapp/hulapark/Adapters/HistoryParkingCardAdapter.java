package com.parkingapp.hulapark.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.parkingapp.hulapark.DataModels.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.R;

import java.util.ArrayList;

public class HistoryParkingCardAdapter extends RecyclerView.Adapter<HistoryParkingCardAdapter.ViewHolder> {
    private MutableLiveData<ArrayList<ParkingCardDataModel>> items = new MutableLiveData<>();
    private Context context;

    public HistoryParkingCardAdapter()
    {
        this.items.setValue(new ArrayList<>());
    }

    public HistoryParkingCardAdapter(ArrayList<ParkingCardDataModel> items)
    {
        this.items.setValue(items);
    }

    public void setCards(ArrayList<ParkingCardDataModel> cardsArray)
    {
        this.items.setValue(cardsArray);
        notifyDataSetChanged();
    }

    public void pushCard(ParkingCardDataModel cardModel)
    {
        if(!items.getValue().contains(cardModel))
        {
            items.getValue().add(cardModel);
            notifyItemInserted(getItemCount());
        }
    }

    public void popCard(ParkingCardDataModel cardModel)
    {
        if (items.getValue().contains(cardModel))
        {
            int position = items.getValue().indexOf(cardModel);
            items.getValue().remove(position);
            notifyItemRemoved(position);
        }
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
        ParkingCardDataModel thisCard = items.getValue().get(position);

        holder.plate_number.setText(thisCard.getPlateNumber());
        holder.location_id.setText(thisCard.getSectorID());
        holder.price.setText(thisCard.getPrice());
        holder.date.setText(thisCard.getDate());
        holder.timespan.setText(thisCard.getTimespan());
    }

    @Override
    public int getItemCount()
    {
        return items.getValue().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView plate_number;
        TextView location_id;
        TextView price;
        TextView date;
        TextView timespan;

        public ViewHolder(View view)
        {
            super(view);
            plate_number = view.findViewById(R.id.history_parkingcard_plate_number);
            location_id = view.findViewById(R.id.history_parkingcard_sector_id);
            price = view.findViewById(R.id.history_parkingcard_price);
            date = view.findViewById(R.id.history_parkingcard_date);
            timespan = view.findViewById(R.id.history_parkingcard_timespan);
        }

    }
}
