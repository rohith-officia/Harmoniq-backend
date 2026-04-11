package com.harmoniq.backend.like.repository;

import com.harmoniq.backend.like.entity.LikedSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {
    boolean existsByUserIdAndSongId(Long userId, Long songId);
    Optional<LikedSong> findByUserIdAndSongId(Long userId, Long songId);
    List<LikedSong> findByUserId(Long userId);
    void deleteBySongId(Long songId);

    @Query("""
        SELECT DISTINCT ls.song.genre
        FROM LikedSong ls
        WHERE ls.user.id = :userId
    """)
    List<String> findDistinctLikedGenresByUserId(Long userId);

    @Query("""
        SELECT ls.song.id
        FROM LikedSong ls
        WHERE ls.user.id = :userId
    """)
    List<Long> findLikedSongIdsByUserId(Long userId);
}