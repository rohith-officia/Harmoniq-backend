package com.harmoniq.backend.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PlaylistResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isPublic;
    private Long userId;
    private String username;
    private List<PlaylistSongResponseDTO> songs;
}