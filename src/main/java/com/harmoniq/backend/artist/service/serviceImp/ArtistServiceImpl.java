package com.harmoniq.backend.artist.service.serviceImp;

import com.harmoniq.backend.artist.dto.ArtistProfileRequestDTO;
import com.harmoniq.backend.artist.dto.ArtistProfileResponseDTO;
import com.harmoniq.backend.artist.entity.ArtistProfile;
import com.harmoniq.backend.artist.repository.ArtistProfileRepository;
import com.harmoniq.backend.artist.service.ArtistService;
import com.harmoniq.backend.common.exception.ArtistProfileAlreadyExistsException;
import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.common.exception.UnauthorizedActionException;
import com.harmoniq.backend.user.entity.Role;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;

    @Override
    public ArtistProfileResponseDTO createArtistProfile(ArtistProfileRequestDTO request) {

        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ARTIST) {
            throw new UnauthorizedActionException("Only users with ARTIST role can create an artist profile");
        }

        if (artistProfileRepository.existsByUser(currentUser)) {
            throw new ArtistProfileAlreadyExistsException("Artist profile already exists for this user");
        }

        ArtistProfile artistProfile = ArtistProfile.builder()
                .displayName(request.getDisplayName())
                .bio(request.getBio())
                .profileImageUrl(request.getProfileImageUrl())
                .bannerImageUrl(request.getBannerImageUrl())
                .user(currentUser)
                .verified(false)
                .monthlyListeners(0L)
                .build();

        ArtistProfile saved = artistProfileRepository.save(artistProfile);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistProfileResponseDTO getMyArtistProfile() {

        User currentUser = getCurrentUser();

        ArtistProfile artistProfile = artistProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Artist profile not found"));

        return mapToResponse(artistProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistProfileResponseDTO getArtistById(Long artistId) {

        ArtistProfile artistProfile = artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist profile not found"));

        return mapToResponse(artistProfile);
    }

    @Override
    public ArtistProfileResponseDTO updateMyArtistProfile(ArtistProfileRequestDTO request) {

        User currentUser = getCurrentUser();

        ArtistProfile artistProfile = artistProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Artist profile not found"));

        artistProfile.setDisplayName(request.getDisplayName());
        artistProfile.setBio(request.getBio());
        artistProfile.setProfileImageUrl(request.getProfileImageUrl());
        artistProfile.setBannerImageUrl(request.getBannerImageUrl());

        ArtistProfile updated = artistProfileRepository.save(artistProfile);

        return mapToResponse(updated);
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ArtistProfileResponseDTO mapToResponse(ArtistProfile artistProfile) {

        return ArtistProfileResponseDTO.builder()
                .id(artistProfile.getId())
                .userId(artistProfile.getUser().getId())
                .username(artistProfile.getUser().getUsername())
                .email(artistProfile.getUser().getEmail())
                .displayName(artistProfile.getDisplayName())
                .bio(artistProfile.getBio())
                .profileImageUrl(artistProfile.getProfileImageUrl())
                .bannerImageUrl(artistProfile.getBannerImageUrl())
                .verified(artistProfile.getVerified())
                .monthlyListeners(artistProfile.getMonthlyListeners())
                .build();
    }
}