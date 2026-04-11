package com.harmoniq.backend.artist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArtistProfileRequestDTO {

    @NotBlank(message = "Display name is required")
    @Size(min = 2, max = 150, message = "Display name must be between 2 and 150 characters")
    private String displayName;

    @Size(max = 2000, message = "Bio cannot exceed 2000 characters")
    private String bio;

    private String profileImageUrl;
    private String bannerImageUrl;
}