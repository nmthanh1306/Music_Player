package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceAlbum;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class APIServiceAlbum {

    public static DataServiceAlbum getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceAlbum.class);
    }
}
