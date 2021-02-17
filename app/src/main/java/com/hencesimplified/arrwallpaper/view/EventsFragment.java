package com.hencesimplified.arrwallpaper.view;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.viewmodel.EventsViewModel;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PhotosViewAdapter photosViewAdapter = new PhotosViewAdapter(new ArrayList<>());
    private EventsViewModel eventsViewModel;
    Button button;

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

        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getPhotos();

        button = view.findViewById(R.id.button);
        recyclerView = view.findViewById(R.id.events_recyclerview);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(photosViewAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        observeViewModel();
    }

    private void observeViewModel() {
        eventsViewModel.listPhotos.observe(getActivity(), photos -> {
            if (photos != null) {
                photosViewAdapter.updatePhotosList(photos);
            }
        });
    }
}
