package com.example.projecta.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projecta.R;
import com.example.projecta.adapter.Adapter_HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<String> title;
    List<Integer> image;
    Adapter_HomeFragment adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_list);

        title = new ArrayList<>();
        image = new ArrayList<>();

        title.add("Waste Bin Locator");
        title.add("Waste Reduction Tips");
        title.add("Complaints");
        title.add("Feedback");
        title.add("Rewards");

        image.add(R.drawable.recyclebin_icon);
        image.add(R.drawable.reduce_icon);
        image.add(R.drawable.complain_icon);
        image.add(R.drawable.feedback_icon);
        image.add(R.drawable.reward_icon);

        adapter = new Adapter_HomeFragment(getActivity(), image, title);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

}