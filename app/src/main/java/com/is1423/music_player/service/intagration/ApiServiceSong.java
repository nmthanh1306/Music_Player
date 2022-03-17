package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceSong;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class ApiServiceSong {

    public static DataServiceSong getService() {
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceSong.class);
    }
}
