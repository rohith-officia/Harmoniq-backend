package com.harmoniq.backend.artist.service;

import com.harmoniq.backend.artist.dto.ArtistProfileRequestDTO;
import com.harmoniq.backend.artist.dto.ArtistProfileResponseDTO;

public interface ArtistService {
    ArtistProfileResponseDTO createArtistProfile(ArtistProfileRequestDTO request);
    ArtistProfileResponseDTO getMyArtistProfile();
    ArtistProfileResponseDTO getArtistById(Long artistId);
    ArtistProfileResponseDTO updateMyArtistProfile(ArtistProfileRequestDTO request);
}