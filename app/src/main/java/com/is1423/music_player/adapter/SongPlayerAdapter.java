package com.is1423.music_player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.model.response.SongResponseDTO;

import java.util.List;

public class SongPlayerAdapter extends RecyclerView.Adapter<SongPlayerAdapter.ViewHolder> {

    private Context context;
    private List<SongResponseDTO> songs;

    public SongPlayerAdapter(Context context, List<SongResponseDTO> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_song_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongResponseDTO song = songs.get(position);
        holder.tvSinger.setText(song.getSinger());
        holder.tvNameSongPlayer.setText(song.getSongName());
        holder.tvIndexSongPlayer.setText((position + 1) + "");
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvIndexSongPlayer;
        private TextView tvNameSongPlayer;
        private TextView tvSinger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView(itemView);
        }

        private void bindingView(View itemView) {
            tvIndexSongPlayer = itemView.findViewById(R.id.tvIndexSongPlayer);
            tvNameSongPlayer = itemView.findViewById(R.id.tvNameSongPlayer);
            tvSinger = itemView.findViewById(R.id.tvSingerNameSongPlayer);
        }
    }
}
