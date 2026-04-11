package com.harmoniq.backend.playlist.service;

import com.harmoniq.backend.playlist.dto.PlaylistRequestDTO;
import com.harmoniq.backend.playlist.dto.PlaylistResponseDTO;

import java.util.List;

public interface PlaylistService {
    PlaylistResponseDTO createPlaylist(PlaylistRequestDTO request);
    List<PlaylistResponseDTO> getMyPlaylists();
    PlaylistResponseDTO getPlaylistById(Long playlistId);
    PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long songId);
    PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long songId);
    String deletePlaylist(Long playlistId);
}