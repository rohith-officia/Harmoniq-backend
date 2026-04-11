package com.harmoniq.backend.history.controller;

import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.history.dto.ListeningHistoryResponseDTO;
import com.harmoniq.backend.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/{songId}")
    public ApiResponse<String> addToHistory(@PathVariable Long songId) {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Song added to history successfully")
                .data(historyService.addToHistory(songId))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<ListeningHistoryResponseDTO>> getMyHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.<List<ListeningHistoryResponseDTO>>builder()
                .success(true)
                .message("Listening history fetched successfully")
                .data(historyService.getMyHistory(page, size))
                .build();
    }

    @GetMapping("/recent")
    public ApiResponse<List<ListeningHistoryResponseDTO>> getRecentHistory(
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.<List<ListeningHistoryResponseDTO>>builder()
                .success(true)
                .message("Recent history fetched successfully")
                .data(historyService.getRecentHistory(limit))
                .build();
    }

    @DeleteMapping("/me")
    public ApiResponse<String> clearMyHistory() {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Listening history cleared successfully")
                .data(historyService.clearMyHistory())
                .build();
    }
}