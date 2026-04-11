package com.harmoniq.backend.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnalyticsOverviewDTO {
    private Long totalUsers;
    private Long totalArtists;
    private Long totalSongs;
    private Long totalPlaylists;
    private Long totalLikes;
    private Long totalHistoryEntries;
    private Long totalPlays;
}