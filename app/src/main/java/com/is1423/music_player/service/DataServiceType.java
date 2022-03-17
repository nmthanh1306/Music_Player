package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.TypeResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataServiceType {

    @GET(Endpoint.Type.GET_RANDOM_4TYPE)
    Call<List<TypeResponseDTO>> getRandom4Types();

    @GET(Endpoint.Type.GET_TYPES_BY_THEME)
    Call<List<TypeResponseDTO>> getTypesByThemeId(@Path("id") Long themeId);
}
