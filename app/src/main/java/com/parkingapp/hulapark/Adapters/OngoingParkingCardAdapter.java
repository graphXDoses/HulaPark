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
import com.parkingapp.hulapark.Utilities.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.ParkingTimeManager;

import java.util.ArrayList;

public class OngoingParkingCardAdapter extends RecyclerView.Adapter<OngoingParkingCardAdapter.ViewHolder> {
    private ArrayList<ParkingCardModel> items;
    private Context context;

    public OngoingParkingCardAdapter()
    {
        this.items = new ArrayList<>();
    }

    public OngoingParkingCardAdapter(ArrayList<ParkingCardModel> items)
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
        View view = LayoutInflater.from(context).inflate(R.layout.rcvi_parking_card_ongoing, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OngoingParkingCardAdapter.ViewHolder holder, int position)
    {
        ParkingCardModel thisCard = items.get(position);

        holder.plate_number.setText(thisCard.getPlateNumber());
        holder.location_id.setText(thisCard.getLocationID());

        holder.charged_hours.setText(thisCard.getChargedHours());
        holder.price.setText(thisCard.getPrice());

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
            }

            @Override
            public boolean onFinish()
            {
                CommonFragUtils.FragmentSwapper.getParkingCardAdapter().popCard(thisCard);
                CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter().pushCard(thisCard);

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
            timecounter_tv = (view.findViewById(R.id.ongoing_parkingcard_timecounter_tv));
            progress = (view.findViewById(R.id.ongoing_parkingcard_progress));
            plate_number = (view.findViewById(R.id.ongoing_parkingcard_plate_number));
            location_id = (view.findViewById(R.id.ongoing_parkingcard_sector_id));
            charged_hours = (view.findViewById(R.id.ongoing_parkingcard_charged_hours));
            price = (view.findViewById(R.id.ongoing_parkingcard_price));
//            view.invalidate();
        }

    }
}
