package com.is1423.music_player.service;

import com.is1423.music_player.constant.Endpoint;
import com.is1423.music_player.model.response.SongResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataServiceSong {

    @GET(Endpoint.Song.TOP_FAVOURITE_SONG)
    Call<List<SongResponseDTO>> getTopFavouriteSongs();

    @GET(Endpoint.Song.GET_SONGS_BY_PLAYLIST_ID)
    Call<List<SongResponseDTO>> getSongsByPlaylistId(@Path("id") Long playlistId);

    @GET(Endpoint.Song.GET_SONGS_BY_TYPE_ID)
    Call<List<SongResponseDTO>> getSongsByTypeId(@Path("id") Long typeId);

    @GET(Endpoint.Song.GET_SONGS_BY_ALBUM_ID)
    Call<List<SongResponseDTO>> getSongsByAlbumId(@Path("id") Long albumId);

    @GET(Endpoint.Song.SEARCH_FULLTEXT_BY_SONG_OR_SINGER)
    Call<List<SongResponseDTO>> searchSongByNameOrSinger(@Query("name") String keyword);
}
