package com.hencesimplified.arrwallpaper.view.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.view.adapters.ConcertPhotosViewAdapter;
import com.hencesimplified.arrwallpaper.viewmodel.ConcertsViewModel;

import java.util.ArrayList;


public class ConcertsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConcertPhotosViewAdapter concertPhotosViewAdapter = new ConcertPhotosViewAdapter(new ArrayList<>());
    private ConcertsViewModel concertsViewModel;

    public ConcertsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_concerts, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 1);
        editor.apply();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        concertsViewModel = new ViewModelProvider(this).get(ConcertsViewModel.class);
        concertsViewModel.getPhotos();

        recyclerView = view.findViewById(R.id.concertRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(concertPhotosViewAdapter);

        observeViewModel();
    }

    private void observeViewModel() {
        concertsViewModel.listPhotos.observe(getActivity(), photos -> {
            if (photos != null) {
                concertPhotosViewAdapter.updatePhotosList(photos);
            }
        });
    }

}
