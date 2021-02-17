package com.hencesimplified.arrwallpaper.view;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.model.SamplePhotos;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstrumentsFragment extends Fragment {

    private List<SamplePhotos> listPhotos;
    private RecyclerView myrv;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerViewAdapter myAdap;

    public InstrumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_instruments, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        listPhotos = new ArrayList<>();
        myrv = root.findViewById(R.id.unlocked_rec_id);
        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myAdap = new RecyclerViewAdapter(getContext(), listPhotos);

        SharedPreferences pref = getContext().getSharedPreferences("ArrPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ArrPage", 2);
        editor.apply();

        databaseReference = firebaseDatabase.getReference("instruments");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SamplePhotos photos = postSnapshot.getValue(SamplePhotos.class);
                    listPhotos.add(photos);
                }

                myrv.setAdapter(myAdap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }


}
