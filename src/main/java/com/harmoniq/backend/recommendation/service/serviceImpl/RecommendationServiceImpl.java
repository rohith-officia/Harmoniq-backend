package com.harmoniq.backend.recommendation.service.serviceImpl;

import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.history.repository.ListeningHistoryRepository;
import com.harmoniq.backend.like.repository.LikedSongRepository;
import com.harmoniq.backend.recommendation.service.RecommendationService;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import com.harmoniq.backend.song.entity.Song;
import com.harmoniq.backend.song.repository.SongRepository;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserRepository userRepository;
    private final LikedSongRepository likedSongRepository;
    private final ListeningHistoryRepository listeningHistoryRepository;
    private final SongRepository songRepository;

    @Override
    public List<SongResponseDTO> getRecommendationsForCurrentUser(int limit) {
        User currentUser = getCurrentUser();

        int safeLimit = Math.min(Math.max(limit, 1), 20);

        List<String> likedGenres = likedSongRepository.findDistinctLikedGenresByUserId(currentUser.getId());
        List<String> historyGenres = listeningHistoryRepository.findDistinctHistoryGenresByUserId(currentUser.getId());

        Set<String> combinedGenres = new LinkedHashSet<>();
        combinedGenres.addAll(likedGenres);
        combinedGenres.addAll(historyGenres);

        if (combinedGenres.isEmpty()) {
            return songRepository.findTrendingSongs(PageRequest.of(0, safeLimit))
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        List<Long> excludedSongIds = likedSongRepository.findLikedSongIdsByUserId(currentUser.getId());

        List<Song> recommendedSongs = songRepository.findRecommendedSongsByGenres(
                new ArrayList<>(combinedGenres),
                excludedSongIds,
                excludedSongIds.isEmpty(),
                PageRequest.of(0, safeLimit)
        );

        if (recommendedSongs.isEmpty()) {
            return songRepository.findTrendingSongs(PageRequest.of(0, safeLimit))
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        return recommendedSongs.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private SongResponseDTO mapToResponse(Song song) {
        return SongResponseDTO.builder()
                .id(song.getId())
                .title(song.getTitle())
                .description(song.getDescription())
                .genre(song.getGenre())
                .audioUrl(song.getAudioUrl())
                .coverImageUrl(song.getCoverImageUrl())
                .durationSeconds(song.getDurationSeconds())
                .published(song.getPublished())
                .playCount(song.getPlayCount())
                .artistId(song.getArtistProfile().getId())
                .artistDisplayName(song.getArtistProfile().getDisplayName())
                .artistUsername(song.getArtistProfile().getUser().getUsername())
                .build();
    }
}