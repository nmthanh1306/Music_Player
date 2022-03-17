package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceAdvertisement;
import com.is1423.music_player.service.api.ApiRetrofitClient;

import lombok.Getter;

/*
* integration ApiRetrofit and DataService
* */

public class ApiServiceAdvertisement {

    public static DataServiceAdvertisement getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceAdvertisement.class);
    }
}
