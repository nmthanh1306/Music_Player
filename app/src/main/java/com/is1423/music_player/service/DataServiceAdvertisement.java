package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.AdvertisementResponseDTO;
import com.is1423.music_player.model.response.SongResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*
* Send method to interact with server and receive data from server
* */
public interface DataServiceAdvertisement {

    @GET(Endpoint.Advertisement.GET_ALL_ADVERTISEMENTS)
    Call<List<AdvertisementResponseDTO>> getAllAdvertisements();

//    @FormUrlEncoded
//    @POST
    @GET(Endpoint.Advertisement.GET_SONGS_BY_ADS_ID)
    Call<List<SongResponseDTO>> getListSongByAdsId(@Path("advertisementId") Long advertisementId);

    @GET(Endpoint.Advertisement.GET_SONGS_BY_ADS_ID)
    Call<SongResponseDTO> getSongByAdsId(@Path("advertisementId") Long advertisementId);
}
