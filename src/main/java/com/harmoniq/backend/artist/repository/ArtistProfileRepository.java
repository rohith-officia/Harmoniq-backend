package com.harmoniq.backend.artist.repository;

import com.harmoniq.backend.artist.entity.ArtistProfile;
import com.harmoniq.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
    Optional<ArtistProfile> findByUser(User user);
    boolean existsByUser(User user);
}