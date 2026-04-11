package com.harmoniq.backend.history.service.serviceImpl;

import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.history.dto.ListeningHistoryResponseDTO;
import com.harmoniq.backend.history.entity.ListeningHistory;
import com.harmoniq.backend.history.repository.ListeningHistoryRepository;
import com.harmoniq.backend.history.service.HistoryService;
import com.harmoniq.backend.song.entity.Song;
import com.harmoniq.backend.song.repository.SongRepository;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private static final int MAX_HISTORY_SIZE = 50;

    private final ListeningHistoryRepository listeningHistoryRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String addToHistory(Long songId) {
        User currentUser = getCurrentUser();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        ListeningHistory history = listeningHistoryRepository
                .findByUserIdAndSongId(currentUser.getId(), songId)
                .map(existing -> {
                    existing.setPlayedAt(LocalDateTime.now());
                    return existing;
                })
                .orElseGet(() -> ListeningHistory.builder()
                        .user(currentUser)
                        .song(song)
                        .playedAt(LocalDateTime.now())
                        .build());

        listeningHistoryRepository.save(history);

        song.setPlayCount(song.getPlayCount() + 1);
        songRepository.save(song);

        trimHistoryIfNeeded(currentUser.getId());

        return "Song added to history successfully";
    }

    @Override
    public List<ListeningHistoryResponseDTO> getMyHistory(int page, int size) {
        User currentUser = getCurrentUser();

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        return listeningHistoryRepository
                .findByUserIdOrderByPlayedAtDesc(currentUser.getId(), PageRequest.of(safePage, safeSize))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ListeningHistoryResponseDTO> getRecentHistory(int limit) {
        User currentUser = getCurrentUser();

        int safeLimit = Math.min(Math.max(limit, 1), 20);

        return listeningHistoryRepository
                .findByUserIdOrderByPlayedAtDesc(currentUser.getId(), PageRequest.of(0, safeLimit))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public String clearMyHistory() {
        User currentUser = getCurrentUser();
        listeningHistoryRepository.deleteByUserId(currentUser.getId());
        return "Listening history cleared successfully";
    }

    private void trimHistoryIfNeeded(Long userId) {
        long total = listeningHistoryRepository.countByUserId(userId);

        if (total <= MAX_HISTORY_SIZE) {
            return;
        }

        List<ListeningHistory> allHistory = listeningHistoryRepository.findByUserIdOrderByPlayedAtDesc(userId);
        List<ListeningHistory> toDelete = allHistory.subList(MAX_HISTORY_SIZE, allHistory.size());

        listeningHistoryRepository.deleteAll(toDelete);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ListeningHistoryResponseDTO mapToResponse(ListeningHistory history) {
        Song song = history.getSong();

        return ListeningHistoryResponseDTO.builder()
                .historyId(history.getId())
                .playedAt(history.getPlayedAt())
                .songId(song.getId())
                .title(song.getTitle())
                .description(song.getDescription())
                .genre(song.getGenre())
                .audioUrl(song.getAudioUrl())
                .coverImageUrl(song.getCoverImageUrl())
                .durationSeconds(song.getDurationSeconds())
                .playCount(song.getPlayCount())
                .artistId(song.getArtistProfile().getId())
                .artistDisplayName(song.getArtistProfile().getDisplayName())
                .artistUsername(song.getArtistProfile().getUser().getUsername())
                .build();
    }
}