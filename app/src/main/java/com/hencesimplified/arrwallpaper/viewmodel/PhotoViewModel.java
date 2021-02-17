package com.hencesimplified.arrwallpaper.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;

import com.hencesimplified.arrwallpaper.view.PhotoViewActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PhotoViewModel extends AndroidViewModel {
    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

    public Target getTarget(final String url) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            File sdCard = Environment.getExternalStorageDirectory();
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

                Toast.makeText(getApplication(), "Downloaded and saved in Internal Storage->ARR Galaxy", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(getApplication(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
