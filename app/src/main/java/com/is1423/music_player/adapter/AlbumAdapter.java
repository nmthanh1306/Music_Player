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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private Context context;
    List<AlbumResponseDTO> albumDTOs;

    public AlbumAdapter(Context context, List<AlbumResponseDTO> albumDTOs) {
        this.context = context;
        this.albumDTOs = albumDTOs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumResponseDTO album = albumDTOs.get(position);
        holder.tvAlbumSingerName.setText(album.getAlbumSinger());
        holder.tvAlbumName.setText(album.getAlbumName());
        Picasso.with(context).load(album.getAlbumImage()).into(holder.imgAlbum);
    }

    @Override
    public int getItemCount() {
        return albumDTOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAlbum;
        TextView tvAlbumName;
        TextView tvAlbumSingerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.ivAlbum);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvAlbumSingerName = itemView.findViewById(R.id.tvAlbumSingerName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ListSongActivity.class);
                    intent.putExtra("album",albumDTOs.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
