package com.parkingapp.hulapark.UserFragments.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parkingapp.hulapark.Adapters.HistoryParkingCardAdapter;
import com.parkingapp.hulapark.Adapters.OngoingParkingCardAdapter;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserStatisticsHistoryFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserStatisticsHistoryFrag extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView history_empty_tv;

    public UserStatisticsHistoryFrag()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserStatisticsHistoryFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserStatisticsHistoryFrag newInstance(String param1, String param2)
    {
        UserStatisticsHistoryFrag fragment = new UserStatisticsHistoryFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_user_statistics_history, container, false);
        history_empty_tv = view.findViewById(R.id.history_empty_tv);
        HistoryParkingCardAdapter adapter = CommonFragUtils.FragmentSwapper.getHistoryParkingCardAdapter();

        // Set Adapter
        RecyclerView rcv = (RecyclerView)view.findViewById(R.id.historyRecycleList);
        rcv.setAdapter(adapter);
        LinearLayoutManager ll = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(ll);

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
            history_empty_tv.setVisibility(View.VISIBLE);
        } else {
            history_empty_tv.setVisibility(View.GONE);
        }
    }
}