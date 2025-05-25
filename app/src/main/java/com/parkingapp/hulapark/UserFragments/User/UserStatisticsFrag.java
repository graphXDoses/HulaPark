package com.parkingapp.hulapark.UserFragments.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Views.TopNavMenuHolderView;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserStatisticsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserStatisticsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TopNavMenuHolderView statsNavMenuHolderView;
    private NavController navController;
    private final HashMap<Integer, Integer> animatorMap = new HashMap<Integer, Integer>();
    private final NavOptions.Builder navBuilder =  new NavOptions.Builder();

//    private TopNavMenuHolderView statsNavMenuHolderView;

    public UserStatisticsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserStatisticsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserStatisticsFrag newInstance(String param1, String param2) {
        UserStatisticsFrag fragment = new UserStatisticsFrag();
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
        View view = inflater.inflate(R.layout.frag_user_statistics, container, false);

        statsNavMenuHolderView = (TopNavMenuHolderView)view.findViewById(R.id.statsNavMenuHolderView);
        statsNavMenuHolderView.attachIndicatorToSelection();

        animatorMap.put(R.id.userStatisticsGenStatsFrag, 0);
        animatorMap.put(R.id.userStatisticsHistoryFrag, 1);

        navController = Navigation.findNavController(view.findViewById(R.id.statsActiveFrag));

        statsNavMenuHolderView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            int index = findItemIndex(id);
            statsNavMenuHolderView.animateIndicatorToIndex(index);

            switch (id)
            {
                case(R.id.stats_top_nav_genstats):
                {
                    navController.navigate(R.id.userStatisticsGenStatsFrag, null, setNavBuilderAnimations(R.id.userStatisticsGenStatsFrag));
                    break;
                }
                case(R.id.stats_top_nav_history):
                {
                    navController.navigate(R.id.userStatisticsHistoryFrag, null, setNavBuilderAnimations(R.id.userStatisticsHistoryFrag));
                    break;
                }
            }

            return true;
        });

        return view;
    }

    private int findItemIndex(int itemId) {
        Menu menu = statsNavMenuHolderView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == itemId) return i;
        }
        return -1;
    }

    private NavOptions setNavBuilderAnimations(int targetFragment)
    {
        int currentDestID = (int)navController.getCurrentDestination().getId();
        int currentFragID = animatorMap.get(currentDestID);
        int targetFragID = animatorMap.get(targetFragment);
        int enter_anim = currentFragID > targetFragID ? R.anim.from_left : R.anim.from_right;
        int exit_anim = currentFragID > targetFragID ? R.anim.to_right : R.anim.to_left;

        return navBuilder.setEnterAnim(enter_anim)
                .setExitAnim(exit_anim)
                .setPopUpTo(currentDestID, true)
                .build();
    }
}