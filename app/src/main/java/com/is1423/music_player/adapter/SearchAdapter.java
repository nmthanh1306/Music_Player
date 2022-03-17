package com.is1423.music_player.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.SongPlayerActivity;
import com.is1423.music_player.model.response.MyFavouriteSongResponseDTO;
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.service.DataServiceMyFavouriteSong;
import com.is1423.music_player.service.DataServiceSong;
import com.is1423.music_player.service.intagration.ApiServiceMyFavouriteSong;
import com.is1423.music_player.service.intagration.ApiServiceSong;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<SongResponseDTO> songs;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

    public SearchAdapter(Context context, List<SongResponseDTO> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongResponseDTO song = songs.get(position);
        holder.tvSearchSongName.setText(song.getSongName());
        holder.tvSearchSinger.setText(song.getSinger());
        Picasso.with(context).load(song.getSongImage()).into(holder.ivSearchSong);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSearchSongName;
        private TextView tvSearchSinger;
        private ImageView ivSearchSong;
        private ImageView ivSearchFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView(itemView);
            bindingAction(itemView);
        }

        private void bindingAction(View itemView) {
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, SongPlayerActivity.class);
                intent.putExtra("song", songs.get(getPosition()));
                context.startActivity(intent);
            });

            ivSearchFavourite.setOnClickListener(view -> {
                String userId = sharedPreferences.getString("userId", null);
                ivSearchFavourite.setImageResource(R.drawable.iconloved);
                DataServiceMyFavouriteSong serviceSong = ApiServiceMyFavouriteSong.getService();
                Call<Void> callback
                        = serviceSong.updateNumberOfFavourite(songs.get(getPosition()).getSongId(), userId);
                callback.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(context, "Đã thích", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                ivSearchFavourite.setEnabled(false);
            });
        }

        private void bindingView(View itemView) {
            sharedPreferences = itemView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            prefEditor = sharedPreferences.edit();

            tvSearchSinger = itemView.findViewById(R.id.tvSearchSinger);
            tvSearchSongName = itemView.findViewById(R.id.tvSearchSongName);
            ivSearchFavourite = itemView.findViewById(R.id.ivSearchFavourite);
            ivSearchSong = itemView.findViewById(R.id.ivSearchSong);
        }
    }
}
