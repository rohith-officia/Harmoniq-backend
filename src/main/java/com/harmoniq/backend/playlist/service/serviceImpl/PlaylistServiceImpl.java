package com.harmoniq.backend.playlist.service.serviceImpl;

import com.harmoniq.backend.common.exception.DuplicateResourceException;
import com.harmoniq.backend.common.exception.ForbiddenOperationException;
import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.playlist.dto.PlaylistRequestDTO;
import com.harmoniq.backend.playlist.dto.PlaylistResponseDTO;
import com.harmoniq.backend.playlist.dto.PlaylistSongResponseDTO;
import com.harmoniq.backend.playlist.entity.Playlist;
import com.harmoniq.backend.playlist.entity.PlaylistSong;
import com.harmoniq.backend.playlist.repository.PlaylistRepository;
import com.harmoniq.backend.playlist.repository.PlaylistSongRepository;
import com.harmoniq.backend.playlist.service.PlaylistService;
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
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @Override
    public PlaylistResponseDTO createPlaylist(PlaylistRequestDTO request) {
        User currentUser = getCurrentUser();

        Playlist playlist = Playlist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .user(currentUser)
                .build();

        Playlist saved = playlistRepository.save(playlist);
        return mapToResponse(saved);
    }

    @Override
    public List<PlaylistResponseDTO> getMyPlaylists() {
        User currentUser = getCurrentUser();

        return playlistRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PlaylistResponseDTO getPlaylistById(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        User currentUser = getCurrentUser();

        if (!playlist.getIsPublic() && !playlist.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenOperationException("You are not allowed to view this playlist");
        }

        return mapToResponse(playlist);
    }

    @Override
    public PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long songId) {
        User currentUser = getCurrentUser();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (!playlist.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenOperationException("You can modify only your own playlists");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        if (playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            throw new DuplicateResourceException("Song already exists in playlist");
        }

        PlaylistSong playlistSong = PlaylistSong.builder()
                .playlist(playlist)
                .song(song)
                .build();

        playlistSongRepository.save(playlistSong);

        Playlist updatedPlaylist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        return mapToResponse(updatedPlaylist);
    }

    @Override
    public PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long songId) {
        User currentUser = getCurrentUser();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (!playlist.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenOperationException("You can modify only your own playlists");
        }

        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found in playlist"));

        playlistSongRepository.delete(playlistSong);

        Playlist updatedPlaylist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        return mapToResponse(updatedPlaylist);
    }

    @Override
    public String deletePlaylist(Long playlistId) {
        User currentUser = getCurrentUser();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (!playlist.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenOperationException("You can delete only your own playlists");
        }

        playlistRepository.delete(playlist);
        return "Playlist deleted successfully";
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private PlaylistResponseDTO mapToResponse(Playlist playlist) {
        List<PlaylistSongResponseDTO> songs = playlistSongRepository.findByPlaylistId(playlist.getId())
                .stream()
                .map(playlistSong -> PlaylistSongResponseDTO.builder()
                        .songId(playlistSong.getSong().getId())
                        .title(playlistSong.getSong().getTitle())
                        .genre(playlistSong.getSong().getGenre())
                        .audioUrl(playlistSong.getSong().getAudioUrl())
                        .coverImageUrl(playlistSong.getSong().getCoverImageUrl())
                        .durationSeconds(playlistSong.getSong().getDurationSeconds())
                        .artistDisplayName(playlistSong.getSong().getArtistProfile().getDisplayName())
                        .build())
                .toList();

        return PlaylistResponseDTO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .isPublic(playlist.getIsPublic())
                .userId(playlist.getUser().getId())
                .username(playlist.getUser().getUsername())
                .songs(songs)
                .build();
    }
}