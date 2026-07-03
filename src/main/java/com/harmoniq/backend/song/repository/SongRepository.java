package com.harmoniq.backend.song.repository;

import com.harmoniq.backend.analytics.dto.GenreStatsDTO;
import com.harmoniq.backend.song.entity.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {

    @EntityGraph(attributePaths = {
            "artistProfile",
            "artistProfile.user"
    })
    List<Song> findByArtistProfileId(Long artistId);

    @EntityGraph(attributePaths = {
            "artistProfile",
            "artistProfile.user"
    })
    @Query("SELECT s FROM Song s ORDER BY s.playCount DESC")
    List<Song> findTrendingSongs(Pageable pageable);

    @EntityGraph(attributePaths = {
            "artistProfile",
            "artistProfile.user"
    })
    @Query("SELECT s FROM Song s WHERE s.artistProfile.id = :artistId ORDER BY s.playCount DESC")
    List<Song> findTopSongsByArtistId(Long artistId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "artistProfile",
            "artistProfile.user"
    })
    Optional<Song> findById(Long id);

    @Query("SELECT COALESCE(SUM(s.playCount),0) FROM Song s")
    Long getTotalPlayCount();

    @Query("""
        SELECT new com.harmoniq.backend.analytics.dto.GenreStatsDTO(
            s.genre,
            COUNT(s),
            COALESCE(SUM(s.playCount),0)
        )
        FROM Song s
        GROUP BY s.genre
        ORDER BY COALESCE(SUM(s.playCount),0) DESC
    """)
    List<GenreStatsDTO> getTopGenres(Pageable pageable);

    @EntityGraph(attributePaths = {
            "artistProfile",
            "artistProfile.user"
    })
    @Query("""
        SELECT s FROM Song s
        WHERE s.genre IN :genres
          AND (:excludeEmpty = true OR s.id NOT IN :excludedSongIds)
        ORDER BY s.playCount DESC, s.createdAt DESC
    """)
    List<Song> findRecommendedSongsByGenres(
            List<String> genres,
            List<Long> excludedSongIds,
            boolean excludeEmpty,
            Pageable pageable
    );

    @Query("""
        SELECT s.genre as genre, COUNT(s) as count
        FROM Song s
        GROUP BY s.genre
        ORDER BY COUNT(s) DESC
    """)
    List<Map<String, Object>> findTopGenres();
}