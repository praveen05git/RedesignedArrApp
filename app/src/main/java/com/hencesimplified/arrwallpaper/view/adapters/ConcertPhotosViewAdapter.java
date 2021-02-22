package com.hencesimplified.arrwallpaper.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.model.PhotoData;
import com.hencesimplified.arrwallpaper.view.fragments.ConcertsFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConcertPhotosViewAdapter extends RecyclerView.Adapter<ConcertPhotosViewAdapter.PhotosViewHolder> {

    private List<PhotoData> photosList;

    public ConcertPhotosViewAdapter(List<PhotoData> photosList) {
        this.photosList = photosList;
    }

    public void updatePhotosList(List<PhotoData> newPhotosList) {
        photosList.clear();
        photosList.addAll(newPhotosList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout_photos, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder holder, final int position) {
        TextView photoTitle = holder.itemView.findViewById(R.id.title_id);
        ImageView photoThumbnail = holder.itemView.findViewById(R.id.photo_id);
        CardView photoCardView = holder.itemView.findViewById(R.id.card_id);

        photoTitle.setText(photosList.get(position).getName());

        Picasso.get()
                .load(photosList.get(position).getUrl())
                .fit()
                .centerCrop()
                .into(photoThumbnail);

        photoCardView.setOnClickListener(view -> {
            String img = photosList.get(position).getUrl();

            ConcertsFragmentDirections.ConcertToPhotos concertToPhotos = ConcertsFragmentDirections.concertToPhotos();
            concertToPhotos.setPhotoUrl(img);
            Navigation.findNavController(photoCardView).navigate(concertToPhotos);
        });

    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public static class PhotosViewHolder extends RecyclerView.ViewHolder {

        public View itemView;

        public PhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;

        }
    }

}