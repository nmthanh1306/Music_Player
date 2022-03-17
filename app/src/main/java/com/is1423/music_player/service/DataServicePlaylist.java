package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.PlaylistResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataServicePlaylist {

    @GET(Endpoint.Playlist.GET_PLAYLISTS_WAS_RANDOM)
    Call<List<PlaylistResponseDTO>> get3PlaylistWasRandomCurrentDate();

    @GET(Endpoint.Playlist.PLAY_LIST_END_POINT)
    Call<List<PlaylistResponseDTO>> getAllPlaylists();
}
