package com.parkingapp.hulapark.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ActionCardDataModel;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.BalanceIncCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.BalanceIncCardViewHolder;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.ParkingCardViewHolder;

import java.util.ArrayList;

public class HistoryParkingCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MutableLiveData<ArrayList<ActionCardDataModel>> items = new MutableLiveData<>();
    private Context context;

    public HistoryParkingCardAdapter()
    {
        this.items.setValue(new ArrayList<>());
    }

    public HistoryParkingCardAdapter(ArrayList<ActionCardDataModel> items)
    {
        this.items.setValue(items);
    }

    public void setCards(ArrayList<? extends ActionCardDataModel> cardsArray)
    {
        this.items.setValue((ArrayList<ActionCardDataModel>)cardsArray);
        notifyDataSetChanged();
    }

    public void pushCard(ActionCardDataModel cardModel)
    {
        if(!items.getValue().contains(cardModel))
        {
            items.getValue().add(cardModel);
            notifyItemInserted(getItemCount());
        }
    }

    public void popCard(ActionCardDataModel cardModel)
    {
        if (items.getValue().contains(cardModel))
        {
            int position = items.getValue().indexOf(cardModel);
            items.getValue().remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return items.getValue().get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rcvi_card_history, parent, false);

        FrameLayout dynamicLayout = view.findViewById(R.id.card_history_dynamic_layout);

        if (viewType == 0)
        {
                LayoutInflater.from(context).inflate(R.layout.inc_card_history_parking, dynamicLayout, true);
                return new ParkingCardViewHolder(view);
        }
        LayoutInflater.from(context).inflate(R.layout.inc_card_history_balanceinc, dynamicLayout, true);
        return new BalanceIncCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ActionCardDataModel thisCard = items.getValue().get(position);

        if (holder instanceof ParkingCardViewHolder && thisCard instanceof ParkingCardDataModel)
            ((ParkingCardViewHolder) holder).bind((ParkingCardDataModel) thisCard);
        else if (holder instanceof BalanceIncCardViewHolder && thisCard instanceof BalanceIncCardDataModel)
            ((BalanceIncCardViewHolder) holder).bind((BalanceIncCardDataModel) thisCard);
    }

    @Override
    public int getItemCount()
    {
        return items.getValue().size();
    }
}
