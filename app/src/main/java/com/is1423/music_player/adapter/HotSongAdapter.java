package com.is1423.music_player.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.AllPlaylistActivity;
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
    private DataServicePlaylist dataServicePlaylist = ApiServicePlaylist.getService();
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

        private ImageView btnAdd;
        private TextView tvMyPlaylistTitle;
        private Spinner spinnerPlaylistName;
        private EditText edtPlaylistName;
        private Button btnConfirmAddPlaylist;

        private List<PlaylistResponseDTO> playlists;
        private PlaylistResponseDTO responsePlaylist;
        private PlaylistRequestDTO playlistRequest;

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
            ivAddToMyPlaylist.setOnClickListener(this::onClickBtnAddToPlaylist);
        }

        private void onClickBtnAddToPlaylist(View view) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.style_my_playlist);

            btnAdd = dialog.findViewById(R.id.btnAdd);
            tvMyPlaylistTitle = dialog.findViewById(R.id.tvMyPlaylistTitle);
            spinnerPlaylistName = dialog.findViewById(R.id.spinnerPlaylistName);
            edtPlaylistName = dialog.findViewById(R.id.edtPlaylistName);
            btnConfirmAddPlaylist = dialog.findViewById(R.id.btnConfirmAddPlaylist);
            fetchDataPlaylistToSpanner();
            eventClickCloseDialogButton(dialog);
            eventClickAddNewPlaylist(dialog);
            eventClickConfirmAddPlaylist(dialog);
        }

        private void eventClickConfirmAddPlaylist(Dialog dialog) {
            playlistRequest = new PlaylistRequestDTO();
            btnConfirmAddPlaylist.setOnClickListener(view -> {

                if (spinnerPlaylistName.isShown()) {
                    PlaylistResponseDTO playlistSpanner = (PlaylistResponseDTO) spinnerPlaylistName.getSelectedItem();
                    playlistRequest.setPlaylistId(playlistSpanner.getPlaylistId());
                    playlistRequest.setPlaylistName(playlistSpanner.getPlaylistName());
                    playlistRequest.setPlaylistImage(hotSongs.get(getLayoutPosition()).getSongImage());
                    updateImagePlaylist(playlistRequest);
                    addSongToPlaylist(hotSongs.get(getLayoutPosition()).getSongId(),
                            playlistSpanner.getPlaylistId(), userId, playlistSpanner.getPlaylistName(), dialog);
                } else { //create new playlist
                    String sPlaylistName = edtPlaylistName.getText().toString();
                    if (sPlaylistName.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập tên playlist", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    playlistRequest.setPlaylistName(sPlaylistName);
                    playlistRequest.setPlaylistImage(hotSongs.get(getLayoutPosition()).getSongImage());
                    createNewPlaylist(playlistRequest, userId, dialog);
                }

            });
        }

        private void updateImagePlaylist(PlaylistRequestDTO request) {
            DataServicePlaylist servicePlaylist = ApiServicePlaylist.getService();
            Call<PlaylistResponseDTO> callback = servicePlaylist.updateMyPlaylist(request, userId);
            callback.enqueue(new Callback<PlaylistResponseDTO>() {
                @Override
                public void onResponse(Call<PlaylistResponseDTO> call, Response<PlaylistResponseDTO> response) {

                }

                @Override
                public void onFailure(Call<PlaylistResponseDTO> call, Throwable t) {

                }
            });
        }

        private void createNewPlaylist(PlaylistRequestDTO requestDTO, String userId, Dialog dialog) {
            Call<PlaylistResponseDTO> callBack = dataServicePlaylist.createMyPlaylist(requestDTO, userId);
            callBack.enqueue(new Callback<PlaylistResponseDTO>() {
                @Override
                public void onResponse(Call<PlaylistResponseDTO> call, Response<PlaylistResponseDTO> response) {
                    if (response.code() == 200) {
                        responsePlaylist = response.body();
                        addSongToPlaylist(hotSongs.get(getLayoutPosition()).getSongId(),
                                responsePlaylist.getPlaylistId(), userId, responsePlaylist.getPlaylistName(), dialog);
                    }
                    if (response.code() == 400) {
                        Toast.makeText(context, "Playlist " +
                                requestDTO.getPlaylistName() + " đã tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PlaylistResponseDTO> call, Throwable t) {

                }
            });
        }

        private void addSongToPlaylist(Long songId, Long playlistId, String userId, String playlistName, Dialog dialog) {
            DataServiceSong serviceSong = ApiServiceSong.getService();
            Call<Void> callBack = serviceSong.addSongToPlaylist(songId, playlistId, userId);
            callBack.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Đã thêm vào playlist: " +
                                playlistName, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    if (response.code() == 400) {
                        Toast.makeText(context, "Bài hát đã tồn tại trong playlist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

        private void fetchDataPlaylistToSpanner() {
            Call<List<PlaylistResponseDTO>> callBack = dataServicePlaylist.getAllPlaylists(userId);
            callBack.enqueue(new Callback<List<PlaylistResponseDTO>>() {
                @Override
                public void onResponse(Call<List<PlaylistResponseDTO>> call, Response<List<PlaylistResponseDTO>> response) {
                    playlists = response.body();
                    ArrayAdapter playlistAdapter = new ArrayAdapter(
                            context, R.layout.support_simple_spinner_dropdown_item, playlists);
                    spinnerPlaylistName.setAdapter(playlistAdapter);
                }

                @Override
                public void onFailure(Call<List<PlaylistResponseDTO>> call, Throwable t) {

                }
            });
        }

        private void eventClickCloseDialogButton(Dialog dialog) {
            ImageView ivClose = dialog.findViewById(R.id.btnClose);
            ivClose.setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }

        private void eventClickAddNewPlaylist(Dialog dialog) {
            btnAdd.setOnClickListener(view -> {
                tvMyPlaylistTitle.setVisibility(View.VISIBLE);
                tvMyPlaylistTitle.setText("Thêm playlist mới");
                spinnerPlaylistName.setVisibility(View.GONE);
                edtPlaylistName.setVisibility(View.VISIBLE);
            });
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

