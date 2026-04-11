package com.harmoniq.backend.song.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SongResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String audioUrl;
    private String coverImageUrl;
    private Integer durationSeconds;
    private Boolean published;
    private Long playCount;

    private Long artistId;
    private String artistDisplayName;
    private String artistUsername;
}