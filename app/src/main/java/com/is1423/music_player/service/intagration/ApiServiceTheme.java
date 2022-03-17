package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceTheme;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class ApiServiceTheme {

    public static DataServiceTheme getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceTheme.class);
    }
}
