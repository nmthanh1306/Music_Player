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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ViewHolder> {

    private Context context;
    private List<SongResponseDTO> listSong;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView(itemView);

            String userId = sharedPreferences.getString("userId",null);

            ivFavouriteInListSong.setOnClickListener(view -> {
                ivFavouriteInListSong.setImageResource(R.drawable.iconloved);
                DataServiceMyFavouriteSong serviceSong = ApiServiceMyFavouriteSong.getService();
                Call<Void> callBack = serviceSong.updateNumberOfFavourite(
                        listSong.get(getLayoutPosition()).getSongId(),userId);
                callBack.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Toast.makeText(context, "Đã thích", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context,"Lỗi!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                ivFavouriteInListSong.setEnabled(false);
            });
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, SongPlayerActivity.class);
                intent.putExtra("song",listSong.get(getPosition()));
                context.startActivity(intent);
            });
        }

        private void bindingView(View itemView) {
            sharedPreferences = itemView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            prefEditor = sharedPreferences.edit();
            tvSingerName = itemView.findViewById(R.id.tvSingerName);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvIndexSong = itemView.findViewById(R.id.tvIndexSong);
            ivFavouriteInListSong = itemView.findViewById(R.id.ivFavouriteInListSong);
        }
    }
}
