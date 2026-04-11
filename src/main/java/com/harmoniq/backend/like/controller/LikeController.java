package com.harmoniq.backend.like.controller;

import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.like.dto.LikedSongResponseDTO;
import com.harmoniq.backend.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{songId}")
    public ApiResponse<String> likeSong(@PathVariable Long songId) {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Song liked successfully")
                .data(likeService.likeSong(songId))
                .build();
    }

    @DeleteMapping("/{songId}")
    public ApiResponse<String> unlikeSong(@PathVariable Long songId) {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Song unliked successfully")
                .data(likeService.unlikeSong(songId))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<LikedSongResponseDTO>> getMyLikedSongs() {
        return ApiResponse.<List<LikedSongResponseDTO>>builder()
                .success(true)
                .message("Liked songs fetched successfully")
                .data(likeService.getMyLikedSongs())
                .build();
    }
}