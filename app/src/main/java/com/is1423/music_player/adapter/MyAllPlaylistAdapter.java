package com.is1423.music_player.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAllPlaylistAdapter extends RecyclerView.Adapter<MyAllPlaylistAdapter.ViewHolder>{
    private int position;
    private Context context;
    private List<PlaylistResponseDTO> playlistResponseDTOS;


    public MyAllPlaylistAdapter(Context context, List<PlaylistResponseDTO> playlistResponseDTOS) {
        this.context = context;
        this.playlistResponseDTOS = playlistResponseDTOS;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_all_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistResponseDTO playlist = playlistResponseDTOS.get(position);
        Picasso.with(context).load(playlist.getPlaylistImage()).into(holder.ivAllPlaylist);
        holder.tvAllPlaylist.setText(playlist.getPlaylistName());
        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return playlistResponseDTOS.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private ImageView ivAllPlaylist;
        private TextView tvAllPlaylist;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAllPlaylist = itemView.findViewById(R.id.ivAllPlaylist);
            tvAllPlaylist = itemView.findViewById(R.id.tvAllPlaylistName);
            bindingAction(itemView);
        }

        private void bindingAction(View itemView) {
            itemView.setOnClickListener(this::onClickItemPlaylist);
            itemView.setOnCreateContextMenuListener(this::onCreateContextMenu);
        }

        private void onClickItemPlaylist(View view) {
            Intent intent = new Intent(context, ListSongActivity.class);
            intent.putExtra("MyItemPlaylist", playlistResponseDTOS.get(getPosition()));
            context.startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            ((Activity)context).getMenuInflater().inflate(R.menu.menu_context_item_playlist, contextMenu);
        }
    }

}
