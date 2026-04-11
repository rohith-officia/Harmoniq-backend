package com.harmoniq.backend.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LikedSongResponseDTO {
    private Long songId;
    private String title;
    private String description;
    private String genre;
    private String audioUrl;
    private String coverImageUrl;
    private Integer durationSeconds;
    private Long playCount;
    private Long artistId;
    private String artistDisplayName;
    private String artistUsername;
}