package com.is1423.music_player.model.request;

import androidx.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlaylistRequestDTO {

    private Long playlistId;

    private String playlistName;

    private String playlistImage;

    private String playlistIcon;
}
