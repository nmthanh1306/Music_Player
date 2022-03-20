package com.is1423.music_player.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.service.DataServiceMyFavouriteSong;
import com.is1423.music_player.service.intagration.ApiServiceMyFavouriteSong;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotSongAdapter extends RecyclerView.Adapter<HotSongAdapter.ViewHolder> {

    Context context;
    private SharedPreferences sharedPreferences;
    private String userId;

    List<SongResponseDTO> hotSongs;

    public HotSongAdapter(Context context, List<SongResponseDTO> hotSongs) {
        this.context = context;
        this.hotSongs = hotSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_hot_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongResponseDTO song = hotSongs.get(position);
        //Log.d("test",song.getSongName());

        holder.tvSongName.setText(song.getSongName());
        holder.tvSingerName.setText(song.getSinger());
        Picasso.with(context).load(song.getSongImage()).into(holder.ivHotSong);
        if (song.isUserFavourite()) {
            holder.ivNumOfFavourite.setImageResource(R.drawable.iconloved);
        } else {
            holder.ivNumOfFavourite.setImageResource(R.drawable.iconlove);
        }
    }

    @Override
    public int getItemCount() {
        return hotSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSongName;
        private TextView tvSingerName;
        private ImageView ivHotSong;
        private ImageView ivNumOfFavourite;
        private ImageView ivAddToMyPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView(itemView);
            bindingAction(itemView);
        }

        private void bindingView(View itemView) {
            sharedPreferences = itemView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("userId", null);

            tvSongName = itemView.findViewById(R.id.tvHotSongName);
            tvSingerName = itemView.findViewById(R.id.tvHotSongSingerName);
            ivHotSong = itemView.findViewById(R.id.ivHotSong);
            ivNumOfFavourite = itemView.findViewById(R.id.ivNumOfFavourite);
            ivAddToMyPlaylist = itemView.findViewById(R.id.ivAddToMyPlaylist);

            if (userId == null) {
                ivNumOfFavourite.setVisibility(View.GONE);
                ivAddToMyPlaylist.setVisibility(View.GONE);
            }

        }

        private void bindingAction(View itemView) {
            ivNumOfFavourite.setOnClickListener(this::onClickImageViewFavourite);
            itemView.setOnClickListener(this::onClickItemView);
        }

        private void onClickItemView(View view) {
            Intent intent = new Intent(context, SongPlayerActivity.class);
            intent.putExtra("song", hotSongs.get(getPosition()));
            context.startActivity(intent);
        }

        private void onClickImageViewFavourite(View view) {
            DataServiceMyFavouriteSong serviceSong = ApiServiceMyFavouriteSong.getService();
            Call<Map<String, Boolean>> callBack = serviceSong.updateNumberOfFavourite(
                    hotSongs.get(getLayoutPosition()).getSongId(), userId);
            callBack.enqueue(new Callback<Map<String, Boolean>>() {
                @Override
                public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                    if (response.code() == 200) {
                        Map<String, Boolean> map = response.body();
                        assert map != null;
                        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                            String k = entry.getKey();
                            Boolean value = entry.getValue();
                            if (k.equals("result")) {
                                if (value) {
                                    Toast.makeText(context, "Đã thích", Toast.LENGTH_SHORT).show();
                                    ivNumOfFavourite.setImageResource(R.drawable.iconloved);
                                } else {
                                    ivNumOfFavourite.setImageResource(R.drawable.iconlove);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Lỗi!", Toast.LENGTH_SHORT).show();
                        ivNumOfFavourite.setImageResource(R.drawable.iconlove);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {

                }
            });
        }
    }
}

