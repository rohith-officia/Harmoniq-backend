package com.harmoniq.backend.analytics.service;

import com.harmoniq.backend.analytics.dto.AnalyticsOverviewDTO;
import com.harmoniq.backend.analytics.dto.GenreStatsDTO;
import com.harmoniq.backend.song.dto.SongResponseDTO;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    List<SongResponseDTO> getTrendingSongs(int limit);
    List<SongResponseDTO> getTopSongsByArtist(Long artistId, int limit);
    AnalyticsOverviewDTO getOverview();
    List<GenreStatsDTO> getTopGenres(int limit);
}