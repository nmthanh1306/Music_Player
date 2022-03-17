package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlbumResponseDTO implements Serializable {
    private Long albumId;

    private String albumName;

    private String albumSinger;

    private String albumImage;
}
