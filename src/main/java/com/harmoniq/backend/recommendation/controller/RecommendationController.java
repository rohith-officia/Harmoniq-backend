package com.harmoniq.backend.recommendation.controller;

import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.recommendation.service.RecommendationService;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/me")
    public ApiResponse<List<SongResponseDTO>> getMyRecommendations(
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.<List<SongResponseDTO>>builder()
                .success(true)
                .message("Recommendations fetched successfully")
                .data(recommendationService.getRecommendationsForCurrentUser(limit))
                .build();
    }
}