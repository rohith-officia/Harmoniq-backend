package com.harmoniq.backend.like.service;

import com.harmoniq.backend.like.dto.LikedSongResponseDTO;

import java.util.List;

public interface LikeService {
    String likeSong(Long songId);
    String unlikeSong(Long songId);
    List<LikedSongResponseDTO> getMyLikedSongs();
}