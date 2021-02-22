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
import com.hencesimplified.arrwallpaper.view.adapters.FamilyPhotosViewAdapter;
import com.hencesimplified.arrwallpaper.viewmodel.FamilyViewModel;

import java.util.ArrayList;


public class FamilyFragment extends Fragment {

    private RecyclerView recyclerView;
    private FamilyPhotosViewAdapter familyPhotosViewAdapter = new FamilyPhotosViewAdapter(new ArrayList<>());
    private FamilyViewModel familyViewModel;

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_family, container, false);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 5);
        editor.apply();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        familyViewModel = new ViewModelProvider(this).get(FamilyViewModel.class);
        familyViewModel.getPhotos();

        recyclerView = view.findViewById(R.id.familyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(familyPhotosViewAdapter);

        observeViewModel();
    }

    private void observeViewModel() {
        familyViewModel.listPhotos.observe(getActivity(), photos -> {
            if (photos != null) {
                familyPhotosViewAdapter.updatePhotosList(photos);
            }
        });
    }

}
