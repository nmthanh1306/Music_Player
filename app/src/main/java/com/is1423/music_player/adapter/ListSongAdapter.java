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
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.activity.SongPlayerActivity;
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.service.DataServiceMyFavouriteSong;
import com.is1423.music_player.service.intagration.ApiServiceMyFavouriteSong;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ViewHolder> {

    private Context context;
    private List<SongResponseDTO> listSong;

    private SharedPreferences sharedPreferences;
    private String userId;

    public ListSongAdapter(Context context, List<SongResponseDTO> listSong) {
        this.context = context;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_list_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongResponseDTO song = listSong.get(position);
        holder.tvIndexSong.setText((position + 1) + "");
        holder.tvSongName.setText(song.getSongName());
        holder.tvSingerName.setText(song.getSinger());

        if (song.isUserFavourite()) {
            holder.ivFavouriteInListSong.setImageResource(R.drawable.iconloved);
        } else {
            holder.ivFavouriteInListSong.setImageResource(R.drawable.iconlove);
        }
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    // khai bao va anh xa views in line_list_song
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSongName;
        private TextView tvSingerName;
        private TextView tvIndexSong;
        private ImageView ivFavouriteInListSong;
        private ImageView ivAddToMyPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView(itemView);
            bindingAction(itemView);
        }

        private void fetchData() {
            DataServiceMyFavouriteSong serviceSong = ApiServiceMyFavouriteSong.getService();
            Call<Map<String, Boolean>> callBack = serviceSong.updateNumberOfFavourite(
                    listSong.get(getLayoutPosition()).getSongId(), userId);
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
                                    ivFavouriteInListSong.setImageResource(R.drawable.iconloved);
                                } else {
                                    ivFavouriteInListSong.setImageResource(R.drawable.iconlove);
//                                    listSong.remove(getLayoutPosition());
//                                    notifyItemRemoved(getLayoutPosition());
//                                    notifyItemRangeChanged(getLayoutPosition(),listSong.size());
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Lỗi!", Toast.LENGTH_SHORT).show();
                        ivFavouriteInListSong.setImageResource(R.drawable.iconlove);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {

                }
            });
        }

        private void bindingAction(View itemView) {

            ivFavouriteInListSong.setOnClickListener(view -> {
                fetchData();
            });
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, SongPlayerActivity.class);
                intent.putExtra("song", listSong.get(getPosition()));
                context.startActivity(intent);
            });
        }

        private void bindingView(View itemView) {
            sharedPreferences = itemView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("userId", null);

            tvSingerName = itemView.findViewById(R.id.tvSingerName);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvIndexSong = itemView.findViewById(R.id.tvIndexSong);
            ivFavouriteInListSong = itemView.findViewById(R.id.ivFavouriteInListSong);
            ivAddToMyPlaylist = itemView.findViewById(R.id.ivAddToMyPlaylist);

            if (userId == null) {
                ivFavouriteInListSong.setVisibility(View.GONE);
                ivAddToMyPlaylist.setVisibility(View.GONE);
            }

        }

    }
}
