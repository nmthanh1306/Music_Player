package com.is1423.music_player.model.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserResponseDTO implements Serializable {
    private Long id;

    private String userName;

    private String password;

    private String role;
}
