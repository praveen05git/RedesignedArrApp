package com.hencesimplified.arrwallpaper.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hencesimplified.arrwallpaper.model.PhotoData;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends AndroidViewModel {

    public MutableLiveData<List<PhotoData>> listPhotos = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<PhotoData> emptyList = new ArrayList<>();

    public EventsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getPhotos() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("event");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PhotoData photos = postSnapshot.getValue(PhotoData.class);
                    emptyList.add(photos);
                    listPhotos.setValue(emptyList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplication(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
