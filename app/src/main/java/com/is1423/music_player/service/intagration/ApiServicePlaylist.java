package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServicePlaylist;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class ApiServicePlaylist {

    public static DataServicePlaylist getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServicePlaylist.class);
    }
}
