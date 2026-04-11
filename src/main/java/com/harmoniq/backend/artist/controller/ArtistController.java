package com.harmoniq.backend.artist.controller;

import com.harmoniq.backend.artist.dto.ArtistProfileRequestDTO;
import com.harmoniq.backend.artist.dto.ArtistProfileResponseDTO;
import com.harmoniq.backend.artist.service.ArtistService;
import com.harmoniq.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping("/me")
    public ApiResponse<ArtistProfileResponseDTO> createArtistProfile(@Valid @RequestBody ArtistProfileRequestDTO request) {
        return ApiResponse.<ArtistProfileResponseDTO>builder()
                .success(true)
                .message("Artist profile created successfully")
                .data(artistService.createArtistProfile(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<ArtistProfileResponseDTO> getMyArtistProfile() {
        return ApiResponse.<ArtistProfileResponseDTO>builder()
                .success(true)
                .message("My artist profile fetched successfully")
                .data(artistService.getMyArtistProfile())
                .build();
    }

    @GetMapping("/{artistId}")
    public ApiResponse<ArtistProfileResponseDTO> getArtistById(@PathVariable Long artistId) {
        return ApiResponse.<ArtistProfileResponseDTO>builder()
                .success(true)
                .message("Artist profile fetched successfully")
                .data(artistService.getArtistById(artistId))
                .build();
    }

    @PutMapping("/me")
    public ApiResponse<ArtistProfileResponseDTO> updateMyArtistProfile(@Valid @RequestBody ArtistProfileRequestDTO request) {
        return ApiResponse.<ArtistProfileResponseDTO>builder()
                .success(true)
                .message("Artist profile updated successfully")
                .data(artistService.updateMyArtistProfile(request))
                .build();
    }
}