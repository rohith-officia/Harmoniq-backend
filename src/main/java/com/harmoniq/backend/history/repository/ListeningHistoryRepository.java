package com.harmoniq.backend.history.repository;

import com.harmoniq.backend.history.entity.ListeningHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    @EntityGraph(attributePaths = {
            "song",
            "song.artistProfile",
            "song.artistProfile.user"
    })
    List<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId);

    @EntityGraph(attributePaths = {
            "song",
            "song.artistProfile",
            "song.artistProfile.user"
    })
    List<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "song",
            "song.artistProfile",
            "song.artistProfile.user"
    })
    Optional<ListeningHistory> findByUserIdAndSongId(Long userId, Long songId);

    void deleteBySongId(Long songId);

    long countByUserId(Long userId);

    void deleteByUserId(Long userId);

    @Query("""
        SELECT DISTINCT lh.song.genre
        FROM ListeningHistory lh
        WHERE lh.user.id = :userId
        ORDER BY lh.song.genre
    """)
    List<String> findDistinctHistoryGenresByUserId(Long userId);
}