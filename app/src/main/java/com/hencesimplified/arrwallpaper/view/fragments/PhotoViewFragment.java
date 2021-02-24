package com.hencesimplified.arrwallpaper.view.fragments;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            imageUrl = PhotoViewFragmentArgs.fromBundle(getArguments()).getPhotoUrl();
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
        photoViewModel.downloadError.observe(getActivity(), error -> {
        });

        photoViewModel.wallpaperError.observe(getActivity(), error -> {
        });
    }

    public void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable) {
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.mipmap.arr_white);
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
