package com.parkingapp.hulapark.Adapters;

import android.content.Context;
import android.os.Bundle;
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
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingTimeManager;

import java.util.ArrayList;

public class OngoingParkingCardAdapter extends RecyclerView.Adapter<OngoingParkingCardAdapter.ViewHolder> {
    private MutableLiveData<ArrayList<ParkingCardDataModel>> items = new MutableLiveData<>();
    private Context context;

    public OngoingParkingCardAdapter()
    {
        this.items.setValue(new ArrayList<>());
    }

    public OngoingParkingCardAdapter(ArrayList<ParkingCardDataModel> items)
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
        View view = LayoutInflater.from(context).inflate(R.layout.rcvi_card_ongoing_parking, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OngoingParkingCardAdapter.ViewHolder holder, int position)
    {
        ParkingCardDataModel thisCard = items.getValue().get(position);

        holder.plate_number.setText(thisCard.getPlateNumber());
        holder.location_id.setText(thisCard.getSectorID());

        holder.charged_hours.setText(thisCard.getChargedHours());
        holder.price.setText(thisCard.getAmount());

        holder.startHour.setText(thisCard.getStartHourString());
        holder.finishHour.setText(thisCard.getFinishHourString());

        holder.infoPanel.setOnClickListener(view -> {
            CommonFragUtils.FragmentSwapper.setMapFocusedPoint(thisCard.getSectorID());
            CommonFragUtils.FragmentSwapper.getHomeActivity().navToSelectedFragUpdateBottomNavBar(R.id.nav_map, null);
        });

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
        return items.getValue().size();
    }

    public LiveData<ArrayList<ParkingCardDataModel>> getLiveData()
    {
        return items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView timecounter_tv;
        CircularProgressIndicator progress;
        TextView plate_number;
        TextView location_id;
        TextView charged_hours;
        TextView price;
        TextView startHour;
        TextView finishHour;
        LinearLayout infoPanel;

        public ViewHolder(View view)
        {
            super(view);
            timecounter_tv = (view.findViewById(R.id.card_ongoing_parking_timecounter_tv));
            progress = (view.findViewById(R.id.card_ongoing_parking_progress));
            plate_number = (view.findViewById(R.id.card_ongoing_parking_plate_number));
            location_id = (view.findViewById(R.id.card_ongoing_parking_sector_id));
            charged_hours = (view.findViewById(R.id.card_ongoing_parking_charged_hours));
            price = (view.findViewById(R.id.card_ongoing_parking_price));
            startHour = (view.findViewById(R.id.card_ongoing_parking_start));
            finishHour = (view.findViewById(R.id.card_ongoing_parking_finish));
            infoPanel = (view.findViewById(R.id.card_ongoing_parking_info_panel));
        }

    }
}
