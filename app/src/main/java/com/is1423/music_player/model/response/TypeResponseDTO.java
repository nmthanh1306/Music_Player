package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TypeResponseDTO implements Serializable {

    private Long typeId;

    private String themeId;

    private String typeName;

    private String typeImage;

}
