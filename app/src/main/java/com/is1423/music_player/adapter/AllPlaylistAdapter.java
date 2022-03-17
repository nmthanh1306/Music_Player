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
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllPlaylistAdapter extends RecyclerView.Adapter<AllPlaylistAdapter.ViewHolder>{

    private Context context;
    private List<PlaylistResponseDTO> playlistResponseDTOS;

    public AllPlaylistAdapter(Context context, List<PlaylistResponseDTO> playlistResponseDTOS) {
        this.context = context;
        this.playlistResponseDTOS = playlistResponseDTOS;
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
        holder.tvAllPlaylist.setText(playlist.getPlayListName());
    }

    @Override
    public int getItemCount() {
        return playlistResponseDTOS.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivAllPlaylist;
        private TextView tvAllPlaylist;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAllPlaylist = itemView.findViewById(R.id.ivAllPlaylist);
            tvAllPlaylist = itemView.findViewById(R.id.tvAllPlaylistName);
            bindingAction(itemView);
        }

        private void bindingAction(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ListSongActivity.class);
                    intent.putExtra("itemPlaylist",playlistResponseDTOS.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
