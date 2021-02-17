package com.hencesimplified.arrwallpaper.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hencesimplified.arrwallpaper.R;
import com.hencesimplified.arrwallpaper.model.PhotoData;
import com.hencesimplified.arrwallpaper.view.activities.PhotoViewActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewFolder> {
    private Context mcontext;
    private List<PhotoData> mdata;

    public RecyclerViewAdapter(Context context, List<PhotoData> listBook) {
        mcontext = context;
        mdata = listBook;
    }


    @NonNull
    @Override
    public MyViewFolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater minflater = LayoutInflater.from(mcontext);
        view = minflater.inflate(R.layout.old_card_view, parent, false);
        return new MyViewFolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewFolder holder, final int position) {

        holder.title.setText(mdata.get(position).getName());

        Picasso.get()
                .load(mdata.get(position).getUrl())
                .fit()
                .centerCrop()
                .into(holder.thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String img = mdata.get(position).getUrl();
                Intent PhotoIntent = new Intent(mcontext, PhotoViewActivity.class);
                PhotoIntent.putExtra("img_url", img);
                mcontext.startActivity(PhotoIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class MyViewFolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;
        CardView cardView;

        public MyViewFolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.photo_title_id);
            thumbnail = itemView.findViewById(R.id.photo_image_id);
            cardView = itemView.findViewById(R.id.card_view_id);

        }
    }

}
