package com.parkingapp.hulapark.UserFragments.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parkingapp.hulapark.Activities.AuthScreen;
import com.parkingapp.hulapark.Activities.BalanceIncScreen;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.DataBase.DBManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards.BalanceIncCardDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Inbound.User.BalanceIncLogDataModel;
import com.parkingapp.hulapark.Utilities.Users.DataSchemas.Outbound.User.NewBalanceIncLogDataModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserWalletFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserWalletFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private Double requestedAmount;

    public UserWalletFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserWalletFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserWalletFrag newInstance(String param1, String param2) {
        UserWalletFrag fragment = new UserWalletFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_user_wallet, container, false);
        AppCompatButton walletAddByTenBtn = view.findViewById(R.id.walletAddByTenBtn);
        AppCompatButton walletAddByTwentyBtn = view.findViewById(R.id.walletAddByTwentyBtn);
        AppCompatButton walletAddByFiftyBtn = view.findViewById(R.id.walletAddByFiftyBtn);

        user = (User)CommonFragUtils.FragmentSwapper.getUser();
        requestedAmount = 0.0;

        user.getBalance().observe(getActivity(), balance -> {
            ((TextView)view.findViewById(R.id.walletCurrentBallance))
                    .setText(String.format("%.2f", balance));
        });

        walletAddByTenBtn.setOnClickListener(__ -> requestBalanceInc(10.0));
        walletAddByTwentyBtn.setOnClickListener(__ -> requestBalanceInc(20.0));
        walletAddByFiftyBtn.setOnClickListener(__ -> requestBalanceInc(50.0));

        CommonFragUtils.FragmentSwapper.authActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent data = result.getData();
                    Boolean shouldInc = data.getBooleanExtra("shouldInc", false);

                    if(shouldInc)
                        balanceIncCallback();
                    else
                        Toast.makeText(getContext(), "Payment canceled..", Toast.LENGTH_SHORT).show();
                }
            }
        );

        return view;
    }

    public void requestBalanceInc(Double amount)
    {
        requestedAmount = amount;
        Intent intent = new Intent(getActivity(), BalanceIncScreen.class).putExtra("INC_AMOUNT", amount);
        CommonFragUtils.FragmentSwapper.authActivityLauncher.launch(intent);
    }

    public void balanceIncCallback()
    {
        LocalDateTime now = LocalDateTime.now();
        NewBalanceIncLogDataModel dataModel = new NewBalanceIncLogDataModel();
        BalanceIncCardDataModel card = new BalanceIncCardDataModel(now);
        long timestamp = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        dataModel.Amount = requestedAmount;
        card.setAmount(String.format("%.2f", dataModel.Amount));
        CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter().pushCard(card);
        double balance = user.getBalance().getValue() + dataModel.Amount;
        user.getBalance().setValue(balance);
        DBManager.updateBalance(user, timestamp, balance, dataModel);
    }
}