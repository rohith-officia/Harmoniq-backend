package com.harmoniq.backend.analytics.controller;

import com.harmoniq.backend.analytics.dto.AnalyticsOverviewDTO;
import com.harmoniq.backend.analytics.dto.GenreStatsDTO;
import com.harmoniq.backend.analytics.service.AnalyticsService;
import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ApiResponse<AnalyticsOverviewDTO> getOverview() {
        return ApiResponse.<AnalyticsOverviewDTO>builder()
                .success(true)
                .message("Analytics overview fetched successfully")
                .data(analyticsService.getOverview())
                .build();
    }

    @GetMapping("/trending")
    public ApiResponse<List<SongResponseDTO>> getTrendingSongs(
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.<List<SongResponseDTO>>builder()
                .success(true)
                .message("Trending songs fetched successfully")
                .data(analyticsService.getTrendingSongs(limit))
                .build();
    }

    @GetMapping("/artists/{artistId}/top-songs")
    public ApiResponse<List<SongResponseDTO>> getTopSongsByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "5") int limit) {
        return ApiResponse.<List<SongResponseDTO>>builder()
                .success(true)
                .message("Top songs by artist fetched successfully")
                .data(analyticsService.getTopSongsByArtist(artistId, limit))
                .build();
    }

    @GetMapping("/top-genres")
    public ApiResponse<List<GenreStatsDTO>> getTopGenres(
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.<List<GenreStatsDTO>>builder()
                .success(true)
                .message("Top genres fetched successfully")
                .data(analyticsService.getTopGenres(limit))
                .build();
    }

}