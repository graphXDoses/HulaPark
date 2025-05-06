package com.parkingapp.hulapark.UserFragments.Guest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parkingapp.hulapark.CommonFragUtils;
import com.parkingapp.hulapark.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestHistoryFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestHistoryFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuestHistoryFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestHistoryFrag newInstance(String param1, String param2) {
        GuestHistoryFrag fragment = new GuestHistoryFrag();
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
        if(CommonFragUtils.FragmentSwapper.getZeta() == 0)
        {
            view = inflater.inflate(R.layout.frag_guest_history, container, false);
        } else {
            view = inflater.inflate(R.layout.frag_user_history, container, false);
        }
        return view;
    }
}