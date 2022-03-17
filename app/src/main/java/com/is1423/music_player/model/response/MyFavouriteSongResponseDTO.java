package com.is1423.music_player.model.response;

import lombok.Data;

@Data
public class MyFavouriteSongResponseDTO {

    private Long id;

    private Long userId;

    private Long songId;
}
