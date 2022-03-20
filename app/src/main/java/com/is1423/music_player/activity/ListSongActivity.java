package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.is1423.music_player.R;
import com.is1423.music_player.adapter.ListSongAdapter;
import com.is1423.music_player.adapter.MyFavouriteSongAdapter;
import com.is1423.music_player.model.response.AdvertisementResponseDTO;
import com.is1423.music_player.model.response.AlbumResponseDTO;
import com.is1423.music_player.model.response.MyFavouriteSongResponseDTO;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.model.response.TypeResponseDTO;
import com.is1423.music_player.service.DataServiceAdvertisement;
import com.is1423.music_player.service.DataServiceSong;
import com.is1423.music_player.service.intagration.ApiServiceAdvertisement;
import com.is1423.music_player.service.intagration.ApiServiceSong;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSongActivity extends AppCompatActivity {

    private AdvertisementResponseDTO advertisement;
    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerViewListSong;
    private FloatingActionButton floatingActionButton;
    private ImageView ivListSong;
    private ListSongAdapter adapter;
    private MyFavouriteSongAdapter myFavouriteSongAdapter;
    List<SongResponseDTO> songs;
    PlaylistResponseDTO playlist;
    TypeResponseDTO type;
    AlbumResponseDTO album;
    MyFavouriteSongResponseDTO myFavouriteSongResponseDTO;

    private SharedPreferences sharedPreferences;
    private String userIdFromSharedPref;
    private String userIdFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // check network signal
        StrictMode.setThreadPolicy(policy);
        getDataFromIntent();
        bindingView();

        if (advertisement != null && !advertisement.getSongName().equals("")) {
            setValueInView(advertisement.getSongName(), advertisement.getSongImage());
            fetchDataAds(advertisement.getAdvertisementId());
        }
        if (playlist != null && !playlist.getPlayListName().equals("")) {
            setValueInView(playlist.getPlayListName(), playlist.getPlaylistImage());
            fetchDataPlaylist(playlist.getPlaylistId(), userIdFromSharedPref);
        }
        if (type != null && !type.getTypeName().equals("")) {
            setValueInView(type.getTypeName(), type.getTypeImage());
            fetchDataType(type.getTypeId(), userIdFromSharedPref);
        }
        if (album != null && !album.getAlbumName().equals("")) {
            setValueInView(album.getAlbumName(), album.getAlbumImage());
            fetchDataAlbum(album.getAlbumId(), userIdFromSharedPref);
        }
        if (userIdFromIntent != null && !userIdFromIntent.equals("")) {
            fetchDataMyFavouriteSong(userIdFromIntent);
        }

    }

    private void fetchDataMyFavouriteSong(String userId) {
        DataServiceSong serviceSong = ApiServiceSong.getService();
        Call<List<SongResponseDTO>> callBack = serviceSong.getFavouriteSongsByUserId(userId);
        callBack.enqueue(new Callback<List<SongResponseDTO>>() {
            @Override
            public void onResponse(Call<List<SongResponseDTO>> call, Response<List<SongResponseDTO>> response) {
                songs = response.body();
                if (songs.size() > 0) {
                    setValueInView("Bài hát yêu thích của bạn", songs.get(0).getSongImage());

                    myFavouriteSongAdapter = new MyFavouriteSongAdapter(ListSongActivity.this, songs);
                    recyclerViewListSong.setLayoutManager(new LinearLayoutManager(ListSongActivity.this));
                    recyclerViewListSong.setAdapter(myFavouriteSongAdapter);
                    eventClickButtonPlayAll();
                }
            }

            @Override
            public void onFailure(Call<List<SongResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void fetchDataAlbum(Long albumId, String userId) {
        DataServiceSong serviceSong = ApiServiceSong.getService();
//        String userIdFromSharedPref = sharedPreferences.getString("userIdFromSharedPref", null);
        Call<List<SongResponseDTO>> callBack = serviceSong.getSongsByAlbumId(albumId, userId);
        callBack.enqueue(new Callback<List<SongResponseDTO>>() {
            @Override
            public void onResponse(Call<List<SongResponseDTO>> call, Response<List<SongResponseDTO>> response) {
                songs = response.body();
                adapter = new ListSongAdapter(ListSongActivity.this, songs);
                recyclerViewListSong.setLayoutManager(new LinearLayoutManager(ListSongActivity.this));
                recyclerViewListSong.setAdapter(adapter);
                eventClickButtonPlayAll();
            }

            @Override
            public void onFailure(Call<List<SongResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void fetchDataType(Long typeId, String userId) {
        DataServiceSong serviceSong = ApiServiceSong.getService();
//        String userIdFromSharedPref = sharedPreferences.getString("userIdFromSharedPref", null);
        Call<List<SongResponseDTO>> callBack = serviceSong.getSongsByTypeId(typeId, userId);
        callBack.enqueue(new Callback<List<SongResponseDTO>>() {
            @Override
            public void onResponse(Call<List<SongResponseDTO>> call, Response<List<SongResponseDTO>> response) {
                songs = response.body();
                adapter = new ListSongAdapter(ListSongActivity.this, songs);
                recyclerViewListSong.setLayoutManager(new LinearLayoutManager(ListSongActivity.this));
                recyclerViewListSong.setAdapter(adapter);
                eventClickButtonPlayAll();
            }

            @Override
            public void onFailure(Call<List<SongResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void fetchDataPlaylist(Long playlistId, String userId) {
        DataServiceSong dataServicePlaylist = ApiServiceSong.getService();
//        String userIdFromSharedPref = sharedPreferences.getString("userIdFromSharedPref", null);
        Call<List<SongResponseDTO>> callBack = dataServicePlaylist.getSongsByPlaylistId(playlistId, userId);
        callBack.enqueue(new Callback<List<SongResponseDTO>>() {
            @Override
            public void onResponse(Call<List<SongResponseDTO>> call, Response<List<SongResponseDTO>> response) {
                songs = response.body();
                if (songs.size() > 0) {
                    adapter = new ListSongAdapter(ListSongActivity.this, songs);
                    recyclerViewListSong.setLayoutManager(new LinearLayoutManager(ListSongActivity.this));
                    recyclerViewListSong.setAdapter(adapter);
                    eventClickButtonPlayAll();
                }
            }

            @Override
            public void onFailure(Call<List<SongResponseDTO>> call, Throwable t) {

            }
        });

    }

    private void fetchDataAds(Long advertisementId) {
        DataServiceAdvertisement dataService = ApiServiceAdvertisement.getService();
        String userId = sharedPreferences.getString("userIdFromSharedPref", null);
        Call<SongResponseDTO> callBack = dataService.getSongByAdsId(advertisementId);
        callBack.enqueue(new Callback<SongResponseDTO>() {
            @Override
            public void onResponse(Call<SongResponseDTO> call, Response<SongResponseDTO> response) {
                //songs = response.body();
                SongResponseDTO song = response.body();

                songs.add(song);
                //Log.d("song", songs.get(0).getSongName());
                adapter = new ListSongAdapter(ListSongActivity.this, songs);
                recyclerViewListSong.setLayoutManager(new LinearLayoutManager(ListSongActivity.this));
                recyclerViewListSong.setAdapter(adapter);
                eventClickButtonPlayAll();
            }

            @Override
            public void onFailure(Call<SongResponseDTO> call, Throwable t) {

            }
        });
    }

    private void setValueInView(String name, String image) {
        collapsingToolbarLayout.setTitle(name);
        try {
            URL url = new URL(image);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            collapsingToolbarLayout.setBackground(bitmapDrawable);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Picasso.with(this).load(image).into(ivListSong);
    }

    private void bindingView() {
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userIdFromSharedPref = sharedPreferences.getString("userIdFromSharedPref", null);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.toolBarListSong);
        recyclerViewListSong = findViewById((R.id.recyclerListSong));
        floatingActionButton = findViewById(R.id.floatingActionButton);
        ivListSong = findViewById(R.id.ivBackgroundListSong);
        songs = new ArrayList<>();
        floatingActionButton.setEnabled(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("banner")) {
                advertisement = (AdvertisementResponseDTO) intent.getSerializableExtra("banner");
                //Log.d("banner", advertisement.getSongName());
            }
            if (intent.hasExtra("itemPlaylist")) {
                playlist = (PlaylistResponseDTO) intent.getSerializableExtra("itemPlaylist");
            }
            if (intent.hasExtra("typeId")) {
                type = (TypeResponseDTO) intent.getSerializableExtra("typeId");
            }
            if (intent.hasExtra("album")) {
                album = (AlbumResponseDTO) intent.getSerializableExtra("album");
            }
            if (intent.hasExtra("myFavouriteSong")) {
                userIdFromIntent = intent.getStringExtra("myFavouriteSong");
            }

        }
    }

    private void eventClickButtonPlayAll() {
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(ListSongActivity.this, SongPlayerActivity.class);
            intent.putParcelableArrayListExtra("songs", (ArrayList<? extends Parcelable>) songs);
            startActivity(intent);
        });
    }
}