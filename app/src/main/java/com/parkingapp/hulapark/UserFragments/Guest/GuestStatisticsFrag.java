package com.parkingapp.hulapark.UserFragments.Guest;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.parkingapp.hulapark.Activities.AuthScreen;
import com.parkingapp.hulapark.Utilities.CommonFragUtils;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.UserType;
import com.parkingapp.hulapark.Views.TopNavMenuHolderView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestStatisticsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestStatisticsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TopNavMenuHolderView statsNavMenuHolderView;

    public GuestStatisticsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestStatistics.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestStatisticsFrag newInstance(String param1, String param2) {
        GuestStatisticsFrag fragment = new GuestStatisticsFrag();
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
            view = inflater.inflate(R.layout.frag_guest_statistics, container, false);
            view.findViewById(R.id.SignInButton).setOnClickListener(view1 -> {
                Intent intent = new Intent(getContext(), AuthScreen.class).putExtra("SIGNUP_INTENT", false);
                startActivity(intent);
            });
            view.findViewById(R.id.SignUpButton).setOnClickListener(view1 -> {
                Intent intent = new Intent(getContext(), AuthScreen.class).putExtra("SIGNUP_INTENT", true);
                startActivity(intent);
            });
        } else {
            view = inflater.inflate(R.layout.frag_user_statistics, container, false);

            statsNavMenuHolderView = (TopNavMenuHolderView)view.findViewById(R.id.statsNavMenuHolderView);
            statsNavMenuHolderView.attachIndicatorToSelection();

            statsNavMenuHolderView.setOnNavigationItemSelectedListener(item -> {
                int index = findItemIndex(item.getItemId());
                statsNavMenuHolderView.animateIndicatorToIndex(index);
                return true;
            });
        }

        return view;
    }

    private int findItemIndex(int itemId) {
        Menu menu = statsNavMenuHolderView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == itemId) return i;
        }
        return -1;
    }
}