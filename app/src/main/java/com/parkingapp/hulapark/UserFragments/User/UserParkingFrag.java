package com.parkingapp.hulapark.UserFragments.User;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Views.LinePagerIndicatorDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserParkingFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserParkingFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView parking_empty_tv;

    public UserParkingFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserParkingFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserParkingFrag newInstance(String param1, String param2) {
        UserParkingFrag fragment = new UserParkingFrag();
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
        View view = inflater.inflate(R.layout.frag_user_parking, container, false);
        parking_empty_tv = view.findViewById(R.id.parking_empty_tv);
        RecyclerView rcv = (RecyclerView)view.findViewById(R.id.parking_card_rc);

        OngoingParkingCardAdapter adapter = CommonFragUtils.FragmentSwapper.getParkingCardAdapter();

        rcv.setAdapter(adapter);
        LinearLayoutManager ll = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv.setLayoutManager(ll);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcv);

        rcv.addItemDecoration(new LinePagerIndicatorDecoration(getResources()));

        updateEmptyMessageVisibility(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateEmptyMessageVisibility(adapter);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                updateEmptyMessageVisibility(adapter);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                updateEmptyMessageVisibility(adapter);
            }
        });

        return view;
    }

    private void updateEmptyMessageVisibility(RecyclerView.Adapter adapter) {
        if (adapter.getItemCount() == 0) {
            parking_empty_tv.setVisibility(View.VISIBLE);
        } else {
            parking_empty_tv.setVisibility(View.GONE);
        }
    }
}