package com.hencesimplified.arrwallpaper.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.model.PhotoData;
import com.hencesimplified.arrwallpaper.util.Util;
import com.hencesimplified.arrwallpaper.view.fragments.CasualFragmentDirections;

import java.util.List;

public class CasualPhotosViewAdapter extends RecyclerView.Adapter<CasualPhotosViewAdapter.PhotosViewHolder> {

    private List<PhotoData> photosList;

    public CasualPhotosViewAdapter(List<PhotoData> photosList) {
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
        ImageView photoThumbnail = holder.itemView.findViewById(R.id.photo_id);
        CardView photoCardView = holder.itemView.findViewById(R.id.card_id);

        RequestOptions options = new RequestOptions()
                .placeholder(Util.getProgressDrawable(photoThumbnail.getContext()))
                .error(R.mipmap.arr_white);
        Glide.with(photoThumbnail.getContext())
                .setDefaultRequestOptions(options)
                .load(photosList.get(position).getUrl())
                .into(photoThumbnail);

        photoCardView.setOnClickListener(view -> {
            String img = photosList.get(position).getUrl();

            CasualFragmentDirections.CasualToPhotos casualToPhotos = CasualFragmentDirections.casualToPhotos();
            casualToPhotos.setPhotoUrl(img);
            Navigation.findNavController(photoCardView).navigate(casualToPhotos);
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
