package com.is1423.music_player.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.activity.SongPlayerActivity;
import com.is1423.music_player.model.request.PlaylistRequestDTO;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.service.DataServiceMyFavouriteSong;
import com.is1423.music_player.service.DataServicePlaylist;
import com.is1423.music_player.service.DataServiceSong;
import com.is1423.music_player.service.intagration.ApiServiceMyFavouriteSong;
import com.is1423.music_player.service.intagration.ApiServicePlaylist;
import com.is1423.music_player.service.intagration.ApiServiceSong;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListSongMyPlaylistAdapter extends RecyclerView.Adapter<ListSongMyPlaylistAdapter.ViewHolder> {

    private Context context;
    private List<SongResponseDTO> listSong;
    private PlaylistResponseDTO playlist;

    private SharedPreferences sharedPreferences;
    private String userId;

    public ListSongMyPlaylistAdapter(Context context, List<SongResponseDTO> listSong, PlaylistResponseDTO playlist) {
        this.context = context;
        this.listSong = listSong;
        this.playlist = playlist;
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
        holder.ivAddToMyPlaylist.setImageResource(R.drawable.ic_action_playlist_added);
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

        private void bindingAction(View itemView) {

            ivFavouriteInListSong.setOnClickListener(this::onClickFavourite);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, SongPlayerActivity.class);
                intent.putExtra("song", listSong.get(getPosition()));
                context.startActivity(intent);
            });

            ivAddToMyPlaylist.setOnClickListener(this::onClickBtnRemoveToPlaylist);
        }

        private void onClickBtnRemoveToPlaylist(View view) {
            DataServiceSong serviceSong = ApiServiceSong.getService();
            Call<Void> callBack = serviceSong.removeSongFromPlaylist(
                    listSong.get(getLayoutPosition()).getSongId(),
                    playlist.getPlaylistId(),
                    userId);
            callBack.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Đã xóa " +
                                listSong.get(getLayoutPosition()).getSongName() +
                                " khỏi playlist", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ListSongActivity.class);
                        intent.putExtra("MyItemPlaylist", playlist);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

        private void onClickFavourite(View view) {
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
