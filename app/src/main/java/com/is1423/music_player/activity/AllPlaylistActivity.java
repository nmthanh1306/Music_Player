package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.AllPlaylistAdapter;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.is1423.music_player.service.DataServicePlaylist;
import com.is1423.music_player.service.intagration.ApiServicePlaylist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPlaylistActivity extends AppCompatActivity {

    private Toolbar toolbarAllPlaylist;
    private RecyclerView recyclerViewAllPlaylist;
    private List<PlaylistResponseDTO> playlists;
    private AllPlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_playlist);
        bindingView();
        bindingAction();
        fetchData();
    }

    private void fetchData() {
        DataServicePlaylist dataServicePlaylist = ApiServicePlaylist.getService();
        Call<List<PlaylistResponseDTO>> callBack = dataServicePlaylist.getAllPlaylists();
        callBack.enqueue(new Callback<List<PlaylistResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PlaylistResponseDTO>> call, Response<List<PlaylistResponseDTO>> response) {
                playlists = response.body();

                adapter = new AllPlaylistAdapter(AllPlaylistActivity.this,playlists);
                recyclerViewAllPlaylist.setLayoutManager(new GridLayoutManager
                        (AllPlaylistActivity.this,2));
                recyclerViewAllPlaylist.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<PlaylistResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void bindingAction() {
        setSupportActionBar(toolbarAllPlaylist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Playlists");
        toolbarAllPlaylist.setTitleTextColor(getResources().getColor(R.color.purple_500));
        toolbarAllPlaylist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void bindingView() {
        toolbarAllPlaylist = findViewById(R.id.toolBarAllPlaylist);
        recyclerViewAllPlaylist = findViewById(R.id.recyclerAllPlaylist);
    }
}