package com.harmoniq.backend.song.service.serviceImpl;

import com.harmoniq.backend.artist.entity.ArtistProfile;
import com.harmoniq.backend.artist.repository.ArtistProfileRepository;
import com.harmoniq.backend.common.exception.ForbiddenOperationException;
import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.history.repository.ListeningHistoryRepository;
import com.harmoniq.backend.like.repository.LikedSongRepository;
import com.harmoniq.backend.playlist.repository.PlaylistRepository;
import com.harmoniq.backend.playlist.repository.PlaylistSongRepository;
import com.harmoniq.backend.song.dto.SongRequestDTO;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import com.harmoniq.backend.song.entity.Song;
import com.harmoniq.backend.song.repository.SongRepository;
import com.harmoniq.backend.song.service.SongService;
import com.harmoniq.backend.user.entity.Role;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.harmoniq.backend.common.file.FileStorageService;
import com.harmoniq.backend.song.dto.SongUploadRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PlaylistSongRepository  playlistSongRepository;
    private final LikedSongRepository  likedSongRepository;
    private final ListeningHistoryRepository  listeningHistoryRepository;

    @Override
    public SongResponseDTO createSong(SongRequestDTO request) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ARTIST) {
            throw new ForbiddenOperationException("Only ARTIST users can create songs");
        }

        ArtistProfile artistProfile = artistProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Artist profile not found"));

        Song song = Song.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .audioUrl(request.getAudioUrl())
                .coverImageUrl(request.getCoverImageUrl())
                .durationSeconds(request.getDurationSeconds())
                .published(true)
                .playCount(0L)
                .artistProfile(artistProfile)
                .build();

        Song saved = songRepository.save(song);
        return mapToResponse(saved);
    }

    @Override
    public List<SongResponseDTO> getAllSongs() {
        return songRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SongResponseDTO getSongById(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        return mapToResponse(song);
    }

    @Override
    public List<SongResponseDTO> getSongsByArtistId(Long artistId) {
        return songRepository.findByArtistProfileId(artistId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SongResponseDTO updateMySong(Long songId, SongRequestDTO request) {
        User currentUser = getCurrentUser();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        if (!song.getArtistProfile().getUser().getEmail().equals(currentUser.getEmail())) {
            throw new ForbiddenOperationException("You can update only your own songs");
        }

        song.setTitle(request.getTitle());
        song.setDescription(request.getDescription());
        song.setGenre(request.getGenre());
        song.setAudioUrl(request.getAudioUrl());
        song.setCoverImageUrl(request.getCoverImageUrl());
        song.setDurationSeconds(request.getDurationSeconds());

        Song updated = songRepository.save(song);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public String deleteMySong(Long songId) {
        User currentUser = getCurrentUser();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        if (!song.getArtistProfile().getUser().getEmail().equals(currentUser.getEmail())) {
            throw new ForbiddenOperationException("You can delete only your own songs");
        }

        playlistSongRepository.deleteBySongId(songId);
        likedSongRepository.deleteBySongId(songId);
        listeningHistoryRepository.deleteBySongId(songId);

        songRepository.delete(song);
        return "Song deleted successfully";
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

    @Override
    public SongResponseDTO uploadSong(SongUploadRequestDTO request, MultipartFile audioFile) {
        User currentUser = getCurrentUser();

        ArtistProfile artistProfile = artistProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Artist profile not found"));

        String storedAudioPath = fileStorageService.storeAudioFile(audioFile);

        Song song = Song.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .coverImageUrl(request.getCoverImageUrl())
                .durationSeconds(request.getDurationSeconds())
                .audioUrl(storedAudioPath)
                .artistProfile(artistProfile)
                .published(true)
                .playCount(0L)
                .build();

        Song savedSong = songRepository.save(song);
        return mapToResponse(savedSong);
    }

    @Override
    public Page<SongResponseDTO> searchSongs(String query, String genre, String sort, int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        Sort sorting;
        if ("playCount".equalsIgnoreCase(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "playCount");
        } else {
            sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(safePage, safeSize, sorting);

        String normalizedQuery = (query == null || query.trim().isEmpty()) ? null : query.trim().toLowerCase();
        String normalizedGenre = (genre == null || genre.trim().isEmpty()) ? null : genre.trim().toLowerCase();

        Specification<Song> spec = (root, cq, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();

            if (normalizedQuery != null) {
                var titlePredicate = cb.like(cb.lower(root.get("title")), "%" + normalizedQuery + "%");
                var artistPredicate = cb.like(
                        cb.lower(root.get("artistProfile").get("displayName")),
                        "%" + normalizedQuery + "%"
                );
                predicates.add(cb.or(titlePredicate, artistPredicate));
            }

            if (normalizedGenre != null) {
                predicates.add(cb.equal(cb.lower(root.get("genre")), normalizedGenre));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return songRepository.findAll(spec, pageable).map(this::mapToResponse);
    }
}