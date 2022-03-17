package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdvertisementResponseDTO implements Serializable {

    private Long advertisementId;

    private Long songId;

    private String advertisementImage;

    private String advertisementContent;

    private String songName;

    private String singer;

    private String songImage;
}