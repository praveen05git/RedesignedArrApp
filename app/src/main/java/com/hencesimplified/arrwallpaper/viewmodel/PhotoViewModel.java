package com.hencesimplified.arrwallpaper.viewmodel;

import android.Manifest;
import android.app.Application;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

public class PhotoViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> downloadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> wallpaperError = new MutableLiveData<>();

    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public void setWallpaper(String imageUrl) {
        Glide.with(getApplication())
                .load(imageUrl)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        WallpaperManager manager = WallpaperManager.getInstance(getApplication());
                        try {
                            manager.setBitmap(bitmap);
                            wallpaperError.setValue(false);
                        } catch (Exception e) {
                            wallpaperError.setValue(true);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        Toast.makeText(getApplication(), "Failed! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void downloadImage(String imageURL) {

        String uniqueID = UUID.randomUUID().toString();
        String fileName = uniqueID + "arr.jpg";
        String mimeType = "image/jpeg";
        String directory = Environment.DIRECTORY_PICTURES + "/ARR Galaxy/";
        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Glide.with(getApplication())
                .load(imageURL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        Toast.makeText(getApplication(), "Saving Image...", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                            values.put(MediaStore.Images.Media.RELATIVE_PATH, directory);

                            ContentResolver resolver = getApplication().getContentResolver();
                            Uri uri = resolver.insert(mediaContentUri, values);
                            try {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, resolver.openOutputStream(uri));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (checkPermission()) {
                                String imagePath = Environment.getExternalStoragePublicDirectory(directory).getAbsolutePath();
                                File image = new File(imagePath, fileName);
                                if (!image.exists()) {
                                    image.mkdirs();
                                }
                                try {
                                    FileOutputStream outputStream = new FileOutputStream(image);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                    Toast.makeText(getApplication(), "Image Downloaded!", Toast.LENGTH_SHORT).show();
                                } catch (FileNotFoundException e) {
                                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplication(), "Not granted permission", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                        Toast.makeText(getApplication(), "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public boolean checkPermission() {
        int check = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
