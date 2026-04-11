package com.harmoniq.backend.playlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlaylistRequestDTO {

    @NotBlank(message = "Playlist name is required")
    @Size(max = 150, message = "Playlist name cannot exceed 150 characters")
    private String name;

    private String description;

    private Boolean isPublic;
}