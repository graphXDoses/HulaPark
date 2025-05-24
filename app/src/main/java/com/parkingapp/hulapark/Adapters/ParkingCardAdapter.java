package com.parkingapp.hulapark.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.parkingapp.hulapark.DataModels.ParkingCardModel;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.ParkingTimeManager;

import java.util.ArrayList;

public class ParkingCardAdapter extends RecyclerView.Adapter<ParkingCardAdapter.ViewHolder> {
    private ArrayList<ParkingCardModel> items;
    private Context context;

    public ParkingCardAdapter()
    {
        this.items = new ArrayList<>();
    }

    public ParkingCardAdapter(ArrayList<ParkingCardModel> items)
    {
        this.items = items;
    }

    public void pushCard(ParkingCardModel cardModel)
    {
        items.add(cardModel);
        notifyItemInserted(getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rcvi_parking_card, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ParkingCardAdapter.ViewHolder holder, int position)
    {
        ParkingCardModel thisCard = items.get(position);
        holder.timecounter_tv.setText(thisCard.getDurationAsString());

        int start = (int)thisCard.getStaticDuration().toMillis();

        thisCard.setOnTickListener(position, new ParkingTimeManager()
        {
            int diff;
            @Override
            public void onTickUpdate(int remainingMillis)
            {
                holder.timecounter_tv.setText(thisCard.getDurationAsString());

                diff = (start - remainingMillis) * 100;
                holder.progress.setProgress(diff / start, true);

                holder.plate_number.setText(thisCard.getPlateNumber());
                holder.location_id.setText(thisCard.getLocationID());

                holder.charged_hours.setText(thisCard.getChargedHours());
                holder.price.setText(thisCard.getPrice());
            }

            @Override
            public boolean onFinish()
            {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    items.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }

                Toast.makeText(context, "Parking Finished!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView timecounter_tv;
        CircularProgressIndicator progress;
        TextView plate_number;
        TextView location_id;
        TextView charged_hours;
        TextView price;

        public ViewHolder(View view)
        {
            super(view);
            timecounter_tv = (view.findViewById(R.id.parkingcard_timecounter_tv));
            progress = (view.findViewById(R.id.parkingcard_progress));
            plate_number = (view.findViewById(R.id.parkingcard_plate_number));
            location_id = (view.findViewById(R.id.parkingcard_location_id));
            charged_hours = (view.findViewById(R.id.parkingcard_charged_hours));
            price = (view.findViewById(R.id.parkingcard_price));
//            view.invalidate();
        }

    }
}
