package com.is1423.music_player.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ThemeAndTypeResponseDTO{

    private List<ThemeResponseDTO> themeResponseDTO;

    private List<TypeResponseDTO> typeResponseDTO;
}
