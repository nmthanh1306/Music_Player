package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceType;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class ApiServiceType {

    public static DataServiceType getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceType.class);
    }
}
