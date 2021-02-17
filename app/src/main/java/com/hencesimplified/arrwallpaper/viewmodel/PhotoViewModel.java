package com.hencesimplified.arrwallpaper.viewmodel;

import android.Manifest;
import android.app.Application;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PhotoViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> downloadError = new MutableLiveData<>();
    public MutableLiveData<Boolean> wallpaperError = new MutableLiveData<>();

    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

    public Target getTarget() {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            String folder_main = "ARR Galaxy";

                            File f = new File(Environment.getExternalStorageDirectory() + "/" + folder_main);

                            if (!f.exists()) {
                                f.mkdirs();
                            }

                            try {

                                String uniqueID = UUID.randomUUID().toString();
                                File file = new File(f.getPath() + "/" + uniqueID + ".jpg");
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.flush();
                                ostream.close();


                            } catch (IOException e) {
                                Log.e("IO", e.getLocalizedMessage());
                            }

                        }


                    }
                }).start();

                downloadError.setValue(false);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                downloadError.setValue(true);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public Target setWallpaper() {
        Target setWallTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                WallpaperManager manager = WallpaperManager.getInstance(getApplication());
                try {
                    manager.setBitmap(bitmap);
                    wallpaperError.setValue(false);
                } catch (Exception e) {
                    wallpaperError.setValue(true);
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return setWallTarget;
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(getApplication(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
