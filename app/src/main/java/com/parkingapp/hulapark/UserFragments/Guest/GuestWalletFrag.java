package com.parkingapp.hulapark.UserFragments.Guest;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parkingapp.hulapark.Activities.AuthScreen;
import com.parkingapp.hulapark.Utilities.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.UserType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestWalletFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestWalletFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuestWalletFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestWalletFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestWalletFrag newInstance(String param1, String param2) {
        GuestWalletFrag fragment = new GuestWalletFrag();
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
        View view;
        if(CommonFragUtils.FragmentSwapper.getUserType() == UserType.GUEST)
        {
            view = inflater.inflate(R.layout.frag_guest_wallet, container, false);
            view.findViewById(R.id.SignInButton).setOnClickListener(view1 -> {
                Intent intent = new Intent(getContext(), AuthScreen.class).putExtra("SIGNUP_INTENT", false);
                startActivity(intent);
            });
            view.findViewById(R.id.SignUpButton).setOnClickListener(view1 -> {
                Intent intent = new Intent(getContext(), AuthScreen.class).putExtra("SIGNUP_INTENT", true);
                startActivity(intent);
            });
        } else {
            view = inflater.inflate(R.layout.frag_user_wallet, container, false);
        }

        return view;
    }
}