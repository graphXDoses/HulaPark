package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards;

import android.view.View;
import android.widget.TextView;
import com.parkingapp.hulapark.R;

import androidx.recyclerview.widget.RecyclerView;

public class ParkingCardViewHolder extends RecyclerView.ViewHolder
{
    TextView plate_number;
    TextView location_id;
    TextView price;
    TextView date;
    TextView timespan;

    public ParkingCardViewHolder(View view)
    {
        super(view);
        plate_number = view.findViewById(R.id.card_history_action_text);
        location_id = view.findViewById(R.id.card_history_action_icon);
        price = view.findViewById(R.id.card_history_amount);
        date = view.findViewById(R.id.card_history_date);
        timespan = view.findViewById(R.id.card_history_timespan);
    }

    public void bind(ParkingCardDataModel thisCard)
    {
        plate_number.setText(thisCard.getPlateNumber());
        location_id.setText(thisCard.getSectorID());
        price.setText(thisCard.getAmount());
        date.setText(thisCard.getDate());
        timespan.setText(thisCard.getStartHourString() + " - " + thisCard.getFinishHourString());
    }
}
