package com.example.phuctd3.swa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phuctd3.swa.HourDetail;
import com.example.phuctd3.swa.HourlyListAdapter;
import com.example.phuctd3.swa.MainActivity;
import com.example.phuctd3.swa.R;

import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HourlyListAdapter mAdapter;



    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mAdapter = new HourlyListAdapter();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.h_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);

        if(MainActivity.getHourlyList() != null) {
            mAdapter.setItems(MainActivity.getHourlyList());
        }
        return view;
    }

    public void setHourlyList(List<HourDetail> list) {
        if(mAdapter != null)
        mAdapter.setItems(list);
    }

    public void setCurrentTemp(String temp) {

    }

}
