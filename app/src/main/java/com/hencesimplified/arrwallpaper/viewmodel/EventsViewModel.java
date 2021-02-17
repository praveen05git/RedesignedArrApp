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
import com.hencesimplified.arrwallpaper.model.SamplePhotos;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends AndroidViewModel {

    public MutableLiveData<List<SamplePhotos>> listPhotos = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<SamplePhotos> emptyList = new ArrayList<>();

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
                    SamplePhotos photos = postSnapshot.getValue(SamplePhotos.class);
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
