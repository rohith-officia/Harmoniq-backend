package com.harmoniq.backend.playlist.repository;

import com.harmoniq.backend.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @EntityGraph(attributePaths = {
            "user"
    })
    List<Playlist> findByUserId(Long userId);

    @Override
    @EntityGraph(attributePaths = {
            "user"
    })
    Optional<Playlist> findById(Long id);
}