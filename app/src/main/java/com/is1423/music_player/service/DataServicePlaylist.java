package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.request.PlaylistRequestDTO;
import com.is1423.music_player.model.response.PlaylistResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataServicePlaylist {

    @GET(Endpoint.Playlist.GET_PLAYLISTS_WAS_RANDOM)
    Call<List<PlaylistResponseDTO>> get3PlaylistWasRandomCurrentDate();

    @GET(Endpoint.Playlist.PLAY_LIST_END_POINT)
    Call<List<PlaylistResponseDTO>> getAllPlaylists(@Query("userId") String userId);

    @POST(Endpoint.Playlist.CREATE_MY_PLAYLIST)
    Call<PlaylistResponseDTO> createMyPlaylist(@Body PlaylistRequestDTO requestDTO,
                                               @Query("userId") String userId);

    @PUT(Endpoint.Playlist.UPDATE_MY_PLAYLIST)
    Call<PlaylistResponseDTO> updateMyPlaylist(@Body PlaylistRequestDTO requestDTO,
                                               @Query("userId") String userId);

    @DELETE(Endpoint.Playlist.DELETE_MY_PLAYLIST)
    Call<Void> deleteMyPlaylist(@Path("id") Long playlistId,
                                               @Query("userId") String userId);
}
