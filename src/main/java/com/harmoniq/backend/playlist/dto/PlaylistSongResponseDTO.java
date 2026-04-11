package com.harmoniq.backend.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PlaylistSongResponseDTO {
    private Long songId;
    private String title;
    private String genre;
    private String audioUrl;
    private String coverImageUrl;
    private Integer durationSeconds;
    private String artistDisplayName;
}