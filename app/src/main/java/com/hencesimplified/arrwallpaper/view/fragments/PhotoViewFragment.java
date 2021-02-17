package com.hencesimplified.arrwallpaper.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.view.PhotoViewFragmentArgs;
import com.hencesimplified.arrwallpaper.viewmodel.PhotoViewModel;
import com.squareup.picasso.Picasso;

public class PhotoViewFragment extends Fragment {

    private ImageView imageView;
    private String imageUrl;
    private Button downloadButton, setWallpaperButton;
    private PhotoViewModel photoViewModel;

    public PhotoViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);
        //setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            imageUrl = PhotoViewFragmentArgs.fromBundle(getArguments()).getPhotoUrl();
        }
        Toast.makeText(getContext(), imageUrl, Toast.LENGTH_LONG).show();

        downloadButton = view.findViewById(R.id.downloadButton);
        setWallpaperButton = view.findViewById(R.id.setWallpaperButton);
        imageView = view.findViewById(R.id.imageViewFg);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);

        photoViewModel.loadImage(imageView, imageUrl);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(imageUrl)
                        .into(photoViewModel.getTarget());
            }
        });

        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert_dia1 = new androidx.appcompat.app.AlertDialog.Builder(getActivity()).create();
                alert_dia1.setTitle("Set as Wallpaper?");
                alert_dia1.setMessage("Are you sure to set this picture as wallpaper?");

                alert_dia1.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Picasso.get().load(imageUrl)
                                .into(photoViewModel.setWallpaper());

                    }
                });
                alert_dia1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert_dia1.show();

            }
        });

        observeViewModel();
    }

    public void observeViewModel() {
        photoViewModel.downloadError.observe(getActivity(), error -> {

        });

        photoViewModel.wallpaperError.observe(getActivity(), error -> {

        });
    }
}
