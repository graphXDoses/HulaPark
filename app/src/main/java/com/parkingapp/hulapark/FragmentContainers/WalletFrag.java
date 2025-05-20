package com.parkingapp.hulapark.FragmentContainers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.AES.AESHelper;
import com.stripe.android.view.CardNumberEditText;
import com.stripe.android.view.CvcEditText;
import com.stripe.android.view.ExpiryDateEditText;
import com.stripe.android.view.PostalCodeEditText;

public class WalletFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WalletFrag() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFrag newInstance(String param1, String param2) {
        WalletFrag fragment = new WalletFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_wallet, container, false);

        CardNumberEditText cardNumberEditText = (CardNumberEditText)view.findViewById(R.id.cardNumberEditText);
        ExpiryDateEditText expiryDateEditText = (ExpiryDateEditText)view.findViewById(R.id.expiryDateEditText);
        CvcEditText cvcEditText = (CvcEditText)view.findViewById(R.id.cvcEditText);
        PostalCodeEditText postalCodeEditText = (PostalCodeEditText)view.findViewById(R.id.postalCodeEditText);

        Button payBtn = view.findViewById(R.id.paymentBtn);
        payBtn.setOnClickListener(v -> {
            String cardNumber = cardNumberEditText.getText().toString();
            String expiryDate = expiryDateEditText.getText().toString();
            String cvc = cvcEditText.getText().toString();
            String postalCode = postalCodeEditText.getText().toString();

            CardInfo cardInfo = new CardInfo(cardNumber, expiryDate, cvc, postalCode);
            Gson gson = new Gson();
            String json = gson.toJson(cardInfo);

            String encrypted;
            String decrypted;
            try {
                encrypted = AESHelper.encrypt(json);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                decrypted = AESHelper.decrypt(encrypted);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            /*System.out.println(cardNumber);
            System.out.println(expiryDate);
            System.out.println(cvc);
            System.out.println(postalCode);*/
            System.out.println(json);
            System.out.println(encrypted);
            System.out.println(decrypted);

            System.out.println("work!!!!!!!!!!!!!!!!!!!!!");
        });

        return view;
    }
}