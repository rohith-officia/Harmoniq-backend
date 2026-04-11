package com.harmoniq.backend.recommendation.service;

import com.harmoniq.backend.song.dto.SongResponseDTO;

import java.util.List;

public interface RecommendationService {
    List<SongResponseDTO> getRecommendationsForCurrentUser(int limit);
}