package com.hencesimplified.arrwallpaper.view.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
        setHasOptionsMenu(true);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 1);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.opt_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int optionId = item.getItemId();

        switch (optionId) {

            case R.id.opt_rate:
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Rate App!");
                alertDialog.setMessage("Are you sure you want to open Play store?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent playStore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hencesimplified.arrwallpaper"));
                        startActivity(playStore);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> alertDialog.dismiss());
                alertDialog.show();
                return true;

            case R.id.opt_more:
                final AlertDialog alertDialogMore = new AlertDialog.Builder(getActivity()).create();
                alertDialogMore.setTitle("More Apps!");
                alertDialogMore.setMessage("Are you sure you want to open Play store?");
                alertDialogMore.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=7031227816779180923")));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Hence Simplified")));
                        }
                    }
                });

                alertDialogMore.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> alertDialogMore.dismiss());
                alertDialogMore.show();
                return true;

            case R.id.opt_about:
                Navigation.findNavController(getView()).navigate(ConcertsFragmentDirections.concertToAbout());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
