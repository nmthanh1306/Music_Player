package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceUser;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class ApiServiceUser {

    public static DataServiceUser getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceUser.class);
    }
}
