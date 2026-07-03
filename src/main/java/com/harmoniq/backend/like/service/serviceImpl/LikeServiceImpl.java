package com.harmoniq.backend.like.service.serviceImpl;

import com.harmoniq.backend.common.exception.DuplicateResourceException;
import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.like.dto.LikedSongResponseDTO;
import com.harmoniq.backend.like.entity.LikedSong;
import com.harmoniq.backend.like.repository.LikedSongRepository;
import com.harmoniq.backend.like.service.LikeService;
import com.harmoniq.backend.song.entity.Song;
import com.harmoniq.backend.song.repository.SongRepository;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikedSongRepository likedSongRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @Override
    public String likeSong(Long songId) {
        User currentUser = getCurrentUser();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        if (likedSongRepository.existsByUserIdAndSongId(currentUser.getId(), songId)) {
            throw new DuplicateResourceException("Song already liked");
        }

        LikedSong likedSong = LikedSong.builder()
                .user(currentUser)
                .song(song)
                .build();

        likedSongRepository.save(likedSong);
        return "Song liked successfully";
    }

    @Override
    public String unlikeSong(Long songId) {
        User currentUser = getCurrentUser();

        LikedSong likedSong = likedSongRepository.findByUserIdAndSongId(currentUser.getId(), songId)
                .orElseThrow(() -> new ResourceNotFoundException("Liked song not found"));

        likedSongRepository.delete(likedSong);
        return "Song unliked successfully";
    }

    @Override
    @Transactional
    public List<LikedSongResponseDTO> getMyLikedSongs() {
        User currentUser = getCurrentUser();

        return likedSongRepository.findByUserId(currentUser.getId())
                .stream()
                .map(likedSong -> mapToResponse(likedSong.getSong()))
                .toList();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private LikedSongResponseDTO mapToResponse(Song song) {
        return LikedSongResponseDTO.builder()
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