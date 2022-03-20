package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.MyFavouriteSongResponseDTO;
import com.is1423.music_player.model.response.SongResponseDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataServiceMyFavouriteSong {

    @PUT(Endpoint.Song.UPDATE_NUM_OF_FAVOURITE)
    Call<Map<String,Boolean>> updateNumberOfFavourite(@Path("id") Long songId, @Query("userId") String userId);
}
