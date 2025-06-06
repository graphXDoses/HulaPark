package com.parkingapp.hulapark.FragmentContainers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Frags.IActiveUserFragSetter;
import com.parkingapp.hulapark.Utilities.Users.UserType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFrag extends Fragment implements IActiveUserFragSetter
{

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
        View view = inflater.inflate(R.layout.frag_wallet, container, false);

        CommonFragUtils.FragmentSwapper.getUserType().observe(getViewLifecycleOwner(), userType -> {
            setActiveUserFrag(userType, view, R.id.walletFragContainer);
        });

        return view;
    }

    @Override
    public void setActiveUserFrag(UserType userType, View thisView, int fragContainer)
    {
        NavController navController = Navigation.findNavController(thisView.findViewById(fragContainer));

        switch (CommonFragUtils.FragmentSwapper.getUserType().getValue())
        {
            case GUEST:
            {
                navController.navigate(Guest.getFragmentContainerActiveFrag(fragContainer));
                break;
            }
            case USER:
            {
                navController.navigate(User.getFragmentContainerActiveFrag(fragContainer));
                break;
            }
        }
    }
}