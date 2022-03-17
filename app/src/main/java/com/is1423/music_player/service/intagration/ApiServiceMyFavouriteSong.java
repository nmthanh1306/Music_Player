package com.is1423.music_player.service.intagration;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.service.DataServiceMyFavouriteSong;
import com.is1423.music_player.service.api.ApiRetrofitClient;

public class ApiServiceMyFavouriteSong {

    public static DataServiceMyFavouriteSong getService(){
        return ApiRetrofitClient.getClient(Endpoint.SERVER_BASE_URL).create(DataServiceMyFavouriteSong.class);
    }
}
