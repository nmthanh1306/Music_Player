package com.is1423.music_player.constant;

public class Endpoint {
    public static final String SERVER_BASE_URL = "http://192.168.1.12:8080/";

    public class Advertisement {

        public static final String ADVERTISEMENTS_END_POINT = "/music-player/api/advertisement";
        public static final String GET_ALL_ADVERTISEMENTS = ADVERTISEMENTS_END_POINT + "/get-all-advertisements";
        public static final String GET_SONGS_BY_ADS_ID = ADVERTISEMENTS_END_POINT + "/{advertisementId}";
    }

    public class Playlist {

        public static final String PLAY_LIST_END_POINT = "/music-player/api/playlist";
        public static final String GET_PLAYLISTS_WAS_RANDOM = PLAY_LIST_END_POINT + "/get3-playlist-random";
        public static final String CREATE_MY_PLAYLIST = PLAY_LIST_END_POINT + "";
        public static final String UPDATE_MY_PLAYLIST = PLAY_LIST_END_POINT + "";
        public static final String DELETE_MY_PLAYLIST = PLAY_LIST_END_POINT + "/{id}";
    }

    public class Theme {
        public static final String MUSIC_THEME_ENDPOINT = "/music-player/api/music-theme";
        public static final String GET_ALL_THEME = MUSIC_THEME_ENDPOINT + "";
        public static final String GET_RANDOM_4THEME = MUSIC_THEME_ENDPOINT + "/random";
    }

    public class Type {
        public static final String TYPE_ENDPOINT = "/music-player/api/type";
        public static final String GET_RANDOM_4TYPE = TYPE_ENDPOINT + ""; // TODO: 3/9/2022
        public static final String GET_TYPES_BY_THEME = TYPE_ENDPOINT + "/theme/{id}";
    }

    public class Album {
        public static final String ALBUM_ENDPOINT = "/music-player/api/album";
        public static final String GET_RANDOM_4ALBUM = ALBUM_ENDPOINT + "/random";
        public static final String GET_ALL_ALBUM = ALBUM_ENDPOINT + "";
    }

    public class Song {
        public static final String SONG_ENDPOINT = "/music-player/api/song";
        public static final String TOP_FAVOURITE_SONG = SONG_ENDPOINT + "/favourite";
        public static final String GET_SONGS_BY_PLAYLIST_ID = SONG_ENDPOINT + "/playlist/{id}";
        public static final String GET_SONGS_BY_TYPE_ID = SONG_ENDPOINT + "/type/{id}";
        public static final String GET_SONGS_BY_ALBUM_ID = SONG_ENDPOINT + "/album/{id}";

        public static final String UPDATE_NUM_OF_FAVOURITE = SONG_ENDPOINT + "/favourite/{id}";
        public static final String SEARCH_FULLTEXT_BY_SONG_OR_SINGER = SONG_ENDPOINT + "/search";
        public static final String GET_FAVOURITE_SONGS_BY_USER = SONG_ENDPOINT + "/favourite/user";
        public static final String ADD_SONG_TO_PLAYLIST = SONG_ENDPOINT + "/playlist/{id}";
        public static final String REMOVE_SONG_FROM_PLAYLIST = SONG_ENDPOINT + "/playlist/{id}";

    }

    public class User {
        public static final String USER_ENDPOINT = "/music-player/api/user";
        public static final String CREATE_USER = USER_ENDPOINT + "/create";
        public static final String LOGIN = USER_ENDPOINT + "/authenticate";
    }
}
