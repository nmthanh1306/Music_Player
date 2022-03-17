package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.request.UserRequestDTO;
import com.is1423.music_player.model.response.UserResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DataServiceUser {

    @POST(Endpoint.User.CREATE_USER)
    Call<UserResponseDTO> createAccount(@Body UserRequestDTO requestDTO);

    @POST(Endpoint.User.LOGIN)
    Call<UserResponseDTO> login(@Body UserRequestDTO requestDTO);
}
