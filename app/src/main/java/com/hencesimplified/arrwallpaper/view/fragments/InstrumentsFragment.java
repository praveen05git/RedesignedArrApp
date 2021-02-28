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
import com.hencesimplified.arrwallpaper.view.adapters.InstrumentPhotosViewAdapter;
import com.hencesimplified.arrwallpaper.viewmodel.InstrumentsViewModel;

import java.util.ArrayList;


public class InstrumentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private InstrumentPhotosViewAdapter instrumentPhotosViewAdapter = new InstrumentPhotosViewAdapter(new ArrayList<>());
    private InstrumentsViewModel instrumentsViewModel;

    public InstrumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_instruments, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 2);
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

        instrumentsViewModel = new ViewModelProvider(this).get(InstrumentsViewModel.class);
        instrumentsViewModel.getPhotos();

        recyclerView = view.findViewById(R.id.instrumentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(instrumentPhotosViewAdapter);

        observeViewModel();
    }

    private void observeViewModel() {
        instrumentsViewModel.listPhotos.observe(getActivity(), photos -> {
            if (photos != null) {
                instrumentPhotosViewAdapter.updatePhotosList(photos);
            }
        });
    }

}
