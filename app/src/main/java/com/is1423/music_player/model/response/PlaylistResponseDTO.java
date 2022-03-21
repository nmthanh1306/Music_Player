package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlaylistResponseDTO implements Serializable {

    private Long playlistId;

    private String playlistName;

    private String playlistImage;

    private String playlistIcon;

    private Long userId;

    @Override
    public String toString() {
        return this.playlistName;
    }
}
