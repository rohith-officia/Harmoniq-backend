package com.harmoniq.backend.playlist.controller;

import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.playlist.dto.PlaylistRequestDTO;
import com.harmoniq.backend.playlist.dto.PlaylistResponseDTO;
import com.harmoniq.backend.playlist.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    public ApiResponse<PlaylistResponseDTO> createPlaylist(@Valid @RequestBody PlaylistRequestDTO request) {
        return ApiResponse.<PlaylistResponseDTO>builder()
                .success(true)
                .message("Playlist created successfully")
                .data(playlistService.createPlaylist(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<PlaylistResponseDTO>> getMyPlaylists() {
        return ApiResponse.<List<PlaylistResponseDTO>>builder()
                .success(true)
                .message("My playlists fetched successfully")
                .data(playlistService.getMyPlaylists())
                .build();
    }

    @GetMapping("/{playlistId}")
    public ApiResponse<PlaylistResponseDTO> getPlaylistById(@PathVariable Long playlistId) {
        return ApiResponse.<PlaylistResponseDTO>builder()
                .success(true)
                .message("Playlist fetched successfully")
                .data(playlistService.getPlaylistById(playlistId))
                .build();
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ApiResponse<PlaylistResponseDTO> addSongToPlaylist(@PathVariable Long playlistId,
                                                              @PathVariable Long songId) {
        return ApiResponse.<PlaylistResponseDTO>builder()
                .success(true)
                .message("Song added to playlist successfully")
                .data(playlistService.addSongToPlaylist(playlistId, songId))
                .build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ApiResponse<PlaylistResponseDTO> removeSongFromPlaylist(@PathVariable Long playlistId,
                                                                   @PathVariable Long songId) {
        return ApiResponse.<PlaylistResponseDTO>builder()
                .success(true)
                .message("Song removed from playlist successfully")
                .data(playlistService.removeSongFromPlaylist(playlistId, songId))
                .build();
    }

    @DeleteMapping("/{playlistId}")
    public ApiResponse<String> deletePlaylist(@PathVariable Long playlistId) {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Playlist deleted successfully")
                .data(playlistService.deletePlaylist(playlistId))
                .build();
    }
}