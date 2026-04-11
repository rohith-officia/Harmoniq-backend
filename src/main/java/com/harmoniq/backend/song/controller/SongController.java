package com.harmoniq.backend.song.controller;

import com.harmoniq.backend.analytics.service.AnalyticsService;
import com.harmoniq.backend.common.response.ApiResponse;
import com.harmoniq.backend.song.dto.SongRequestDTO;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import com.harmoniq.backend.song.repository.SongRepository;
import com.harmoniq.backend.song.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.harmoniq.backend.song.dto.SongUploadRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SongController {

    private final SongService songService;
    private final AnalyticsService analyticsService;

    @PostMapping
    public ApiResponse<SongResponseDTO> createSong(@Valid @RequestBody SongRequestDTO request) {
        return ApiResponse.<SongResponseDTO>builder()
                .success(true)
                .message("Song created successfully")
                .data(songService.createSong(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SongResponseDTO>> getAllSongs() {
        return ApiResponse.<List<SongResponseDTO>>builder()
                .success(true)
                .message("Songs fetched successfully")
                .data(songService.getAllSongs())
                .build();
    }

    @GetMapping("/{songId}")
    public ApiResponse<SongResponseDTO> getSongById(@PathVariable Long songId) {
        return ApiResponse.<SongResponseDTO>builder()
                .success(true)
                .message("Song fetched successfully")
                .data(songService.getSongById(songId))
                .build();
    }

    @GetMapping("/artist/{artistId}")
    public ApiResponse<List<SongResponseDTO>> getSongsByArtistId(@PathVariable Long artistId) {
        return ApiResponse.<List<SongResponseDTO>>builder()
                .success(true)
                .message("Artist songs fetched successfully")
                .data(songService.getSongsByArtistId(artistId))
                .build();
    }

    @PutMapping("/{songId}")
    public ApiResponse<SongResponseDTO> updateMySong(@PathVariable Long songId,
                                                     @Valid @RequestBody SongRequestDTO request) {
        return ApiResponse.<SongResponseDTO>builder()
                .success(true)
                .message("Song updated successfully")
                .data(songService.updateMySong(songId, request))
                .build();
    }

    @DeleteMapping("/{songId}")
    public ApiResponse<String> deleteMySong(@PathVariable Long songId) {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Song deleted successfully")
                .data(songService.deleteMySong(songId))
                .build();
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<SongResponseDTO> uploadSong(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("genre") String genre,
            @RequestParam(value = "coverImageUrl", required = false) String coverImageUrl,
            @RequestParam("durationSeconds") Integer durationSeconds,
            @RequestParam("audioFile") MultipartFile audioFile) {

        SongUploadRequestDTO request = new SongUploadRequestDTO();
        request.setTitle(title);
        request.setDescription(description);
        request.setGenre(genre);
        request.setCoverImageUrl(coverImageUrl);
        request.setDurationSeconds(durationSeconds);

        return ApiResponse.<SongResponseDTO>builder()
                .success(true)
                .message("Song uploaded successfully")
                .data(songService.uploadSong(request, audioFile))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<Page<SongResponseDTO>> searchSongs(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.<Page<SongResponseDTO>>builder()
                .success(true)
                .message("Songs searched successfully")
                .data(songService.searchSongs(query, genre, sort, page, size))
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
}