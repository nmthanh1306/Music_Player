package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.ThemeResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataServiceTheme {
    @GET(Endpoint.Theme.GET_RANDOM_4THEME)
    Call<List<ThemeResponseDTO>> getRandom4Theme();

    @GET(Endpoint.Theme.GET_ALL_THEME)
    Call<List<ThemeResponseDTO>> getAllThemes();
}
