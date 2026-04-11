package com.harmoniq.backend.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ListeningHistoryResponseDTO {
    private Long historyId;
    private LocalDateTime playedAt;

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