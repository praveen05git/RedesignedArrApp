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
import com.hencesimplified.arrwallpaper.view.adapters.EventsPhotosViewAdapter;
import com.hencesimplified.arrwallpaper.viewmodel.EventsViewModel;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventsPhotosViewAdapter eventsPhotosViewAdapter = new EventsPhotosViewAdapter(new ArrayList<>());
    private EventsViewModel eventsViewModel;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 3);
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

        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getPhotos();

        recyclerView = view.findViewById(R.id.events_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(eventsPhotosViewAdapter);

        observeViewModel();
    }

    private void observeViewModel() {
        eventsViewModel.listPhotos.observe(getActivity(), photos -> {
            if (photos != null) {
                eventsPhotosViewAdapter.updatePhotosList(photos);
            }
        });
    }
}
