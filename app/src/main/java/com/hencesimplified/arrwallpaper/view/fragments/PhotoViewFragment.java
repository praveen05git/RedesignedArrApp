package com.hencesimplified.arrwallpaper.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.util.Util;
import com.hencesimplified.arrwallpaper.viewmodel.PhotoViewModel;

public class PhotoViewFragment extends Fragment {

    private ImageView imageView;
    private String imageUrl;
    private Button downloadButton, setWallpaperButton;
    private PhotoViewModel photoViewModel;
    private ConstraintLayout constraintLayout;

    public PhotoViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            imageUrl = PhotoViewFragmentArgs.fromBundle(getArguments()).getPhotoUrl();
        }

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
        }

        downloadButton = view.findViewById(R.id.downloadButton);
        setWallpaperButton = view.findViewById(R.id.setWallpaperButton);
        imageView = view.findViewById(R.id.imageViewFg);
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        constraintLayout = view.findViewById(R.id.constraint);

        loadImage(imageView, imageUrl, Util.getProgressDrawable(imageView.getContext()));
        setBackgroundColor(imageUrl, constraintLayout);

        downloadButton.setOnClickListener(view1 -> photoViewModel.downloadImage(imageUrl));

        setWallpaperButton.setOnClickListener(v -> {
            AlertDialog alert_dia1 = new AlertDialog.Builder(getActivity()).create();
            alert_dia1.setTitle("Set as Wallpaper?");
            alert_dia1.setMessage("Are you sure to set this picture as wallpaper?");

            alert_dia1.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> photoViewModel.setWallpaper(imageUrl));
            alert_dia1.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.cancel());
            alert_dia1.show();

        });

        observeViewModel();
    }

    public void observeViewModel() {
        photoViewModel.isDownloaded.observe(getActivity(), downloaded -> {
            if (downloaded)
                Snackbar.make(getView(), "Wallpaper downloaded Successfully!!", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(getView(), "Download Failed!", Snackbar.LENGTH_SHORT).show();
        });

        photoViewModel.wallpaperSet.observe(getActivity(), wallpaperSet -> {
            if (wallpaperSet)
                Snackbar.make(getView(), "Wallpaper applied successfully!!", Snackbar.LENGTH_SHORT).show();
            else
                Snackbar.make(getView(), "Setting Wallpaper Failed!", Snackbar.LENGTH_SHORT).show();
        });

        photoViewModel.permission.observe(getActivity(), permission -> {
            if (!permission)
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        });
    }

    public boolean checkPermission(String permission) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        int check = ContextCompat.checkSelfPermission(getContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable);
        //.error(R.mipmap.arr_white);
        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(url)
                .into(imageView);
    }

    public void setBackgroundColor(String url, ConstraintLayout constraintLayout) {

        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = 0;
                                    if (palette.getDarkVibrantSwatch() != null) {
                                        intColor = palette.getDarkVibrantSwatch().getRgb();
                                    } else if (palette.getLightMutedSwatch() != null) {
                                        intColor = palette.getLightMutedSwatch().getRgb();
                                    }

                                    constraintLayout.setBackgroundColor(intColor);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

}
