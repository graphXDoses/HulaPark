package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.parkingapp.hulapark.R;

public class BalanceIncCardViewHolder extends RecyclerView.ViewHolder
{
    TextView price;
    TextView date;
    TextView timestamp;

    public BalanceIncCardViewHolder(View view)
    {
        super(view);
        price = view.findViewById(R.id.card_history_amount);
        date = view.findViewById(R.id.card_history_date);
        timestamp = view.findViewById(R.id.card_history_timespan);
    }

    public void bind(BalanceIncCardDataModel thisCard)
    {
        price.setText(thisCard.getAmount());
        date.setText(thisCard.getDate());
        timestamp.setText(thisCard.getTimestampString());
    }
}
