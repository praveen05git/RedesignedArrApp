package com.hencesimplified.arrwallpaper.view.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.view.adapters.CasualPhotosViewAdapter;
import com.hencesimplified.arrwallpaper.viewmodel.CasualViewModel;

import java.util.ArrayList;

public class CasualFragment extends Fragment {

    private RecyclerView recyclerView;
    private CasualPhotosViewAdapter casualPhotosViewAdapter = new CasualPhotosViewAdapter(new ArrayList<>());
    private CasualViewModel casualViewModel;

    public CasualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_casual, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 4);
        editor.apply();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } catch (Exception e) {
        }

        casualViewModel = new ViewModelProvider(this).get(CasualViewModel.class);
        casualViewModel.getPhotos();

        recyclerView = view.findViewById(R.id.casualRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(casualPhotosViewAdapter);

        observeViewModel();
    }

    private void observeViewModel() {
        casualViewModel.listPhotos.observe(getActivity(), photos -> {
            if (photos != null) {
                casualPhotosViewAdapter.updatePhotosList(photos);
            }
        });
    }

}
