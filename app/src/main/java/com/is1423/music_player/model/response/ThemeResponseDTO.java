package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ThemeResponseDTO implements Serializable {

    private Long themeId;

    private String themeName;

    private String themeImage;
}
