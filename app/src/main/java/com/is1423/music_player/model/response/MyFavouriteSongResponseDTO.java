package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class MyFavouriteSongResponseDTO implements Serializable {

    private Long id;

    private Long userId;

    private Long songId;
}
