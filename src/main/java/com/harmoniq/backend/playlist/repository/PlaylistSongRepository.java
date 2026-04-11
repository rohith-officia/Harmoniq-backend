package com.harmoniq.backend.playlist.repository;

import com.harmoniq.backend.playlist.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    List<PlaylistSong> findByPlaylistId(Long playlistId);
    Optional<PlaylistSong> findByPlaylistIdAndSongId(Long playlistId, Long songId);
    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);
    void deleteBySongId(Long songId);
}