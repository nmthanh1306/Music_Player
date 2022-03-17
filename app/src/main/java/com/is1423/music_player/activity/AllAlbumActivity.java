package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.AllAlbumAdapter;
import com.is1423.music_player.model.response.AlbumResponseDTO;
import com.is1423.music_player.service.DataServiceAlbum;
import com.is1423.music_player.service.intagration.APIServiceAlbum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAlbumActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAllAlbum;
    private Toolbar toolbarAllAlbum;
    private AllAlbumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_album);
        bindingView();
        bindingAction();
        fetchData();
    }

    private void fetchData() {
        DataServiceAlbum serviceAlbum = APIServiceAlbum.getService();
        Call<List<AlbumResponseDTO>> callback = serviceAlbum.getAllAlbum();
        callback.enqueue(new Callback<List<AlbumResponseDTO>>() {
            @Override
            public void onResponse(Call<List<AlbumResponseDTO>> call, Response<List<AlbumResponseDTO>> response) {
                List<AlbumResponseDTO> albums = response.body();
                adapter = new AllAlbumAdapter(AllAlbumActivity.this, albums);
                recyclerViewAllAlbum.setLayoutManager(new GridLayoutManager(AllAlbumActivity.this, 2));
                recyclerViewAllAlbum.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<AlbumResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void bindingView() {
        recyclerViewAllAlbum = findViewById(R.id.recyclerAllAlbum);
        toolbarAllAlbum = findViewById(R.id.toolBarAllAlbum);
        setSupportActionBar(toolbarAllAlbum);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Album");
    }

    private void bindingAction() {
        toolbarAllAlbum.setNavigationOnClickListener(view -> finish());
    }
}