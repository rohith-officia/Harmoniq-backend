package com.harmoniq.backend.artist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ArtistProfileResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String profileImageUrl;
    private String bannerImageUrl;
    private Boolean verified;
    private Long monthlyListeners;
}