package com.harmoniq.backend.history.service;

import com.harmoniq.backend.history.dto.ListeningHistoryResponseDTO;

import java.util.List;

public interface HistoryService {
    String addToHistory(Long songId);
    List<ListeningHistoryResponseDTO> getMyHistory(int page, int size);
    List<ListeningHistoryResponseDTO> getRecentHistory(int limit);
    String clearMyHistory();
}