package com.is1423.music_player.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.model.response.AlbumResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllAlbumAdapter extends RecyclerView.Adapter<AllAlbumAdapter.ViewHolder> {

    public AllAlbumAdapter(Context context, List<AlbumResponseDTO> albums) {
        this.context = context;
        this.albums = albums;
    }

    private Context context;
    private List<AlbumResponseDTO> albums;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_all_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumResponseDTO album = albums.get(position);
        Picasso.with(context).load(album.getAlbumImage()).into(holder.ivAllAlbum);
        holder.tvAllAlbumName.setText(album.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAllAlbum;
        private TextView tvAllAlbumName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAllAlbum = itemView.findViewById(R.id.ivAllAlbum);
            tvAllAlbumName = itemView.findViewById(R.id.tvAllAlbumName);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ListSongActivity.class);
                intent.putExtra("album", albums.get(getPosition()));
                context.startActivity(intent);
            });
        }
    }
}
