package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.AlbumResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataServiceAlbum {

    @GET(Endpoint.Album.GET_RANDOM_4ALBUM)
    Call<List<AlbumResponseDTO>> getRandom4AlbumPerDay();

    @GET(Endpoint.Album.GET_ALL_ALBUM)
    Call<List<AlbumResponseDTO>> getAllAlbum();
}
