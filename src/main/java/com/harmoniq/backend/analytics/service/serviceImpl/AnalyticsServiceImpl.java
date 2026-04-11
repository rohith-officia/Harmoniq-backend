package com.harmoniq.backend.analytics.service.serviceImpl;

import com.harmoniq.backend.analytics.dto.AnalyticsOverviewDTO;
import com.harmoniq.backend.analytics.dto.GenreStatsDTO;
import com.harmoniq.backend.analytics.service.AnalyticsService;
import com.harmoniq.backend.artist.repository.ArtistProfileRepository;
import com.harmoniq.backend.history.repository.ListeningHistoryRepository;
import com.harmoniq.backend.like.repository.LikedSongRepository;
import com.harmoniq.backend.playlist.repository.PlaylistRepository;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import com.harmoniq.backend.song.entity.Song;
import com.harmoniq.backend.song.repository.SongRepository;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final PlaylistRepository playlistRepository;
    private final LikedSongRepository likedSongRepository;
    private final ListeningHistoryRepository listeningHistoryRepository;

    @Override
    public List<SongResponseDTO> getTrendingSongs(int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), 20);

        return songRepository.findTrendingSongs(PageRequest.of(0, safeLimit))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SongResponseDTO> getTopSongsByArtist(Long artistId, int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), 20);

        return songRepository.findTopSongsByArtistId(artistId, PageRequest.of(0, safeLimit))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AnalyticsOverviewDTO getOverview() {
        Long totalPlays = songRepository.getTotalPlayCount();

        return AnalyticsOverviewDTO.builder()
                .totalUsers(userRepository.count())
                .totalArtists(artistProfileRepository.count())
                .totalSongs(songRepository.count())
                .totalPlaylists(playlistRepository.count())
                .totalLikes(likedSongRepository.count())
                .totalHistoryEntries(listeningHistoryRepository.count())
                .totalPlays(totalPlays != null ? totalPlays : 0L)
                .build();
    }

    @Override
    public List<GenreStatsDTO> getTopGenres(int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), 20);
        return songRepository.getTopGenres(PageRequest.of(0, safeLimit));
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