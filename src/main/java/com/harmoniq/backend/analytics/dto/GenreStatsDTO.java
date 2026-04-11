package com.harmoniq.backend.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GenreStatsDTO {
    private String genre;
    private Long totalSongs;
    private Long totalPlays;
}