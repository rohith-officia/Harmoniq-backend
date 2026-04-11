package com.harmoniq.backend.song.service;

import com.harmoniq.backend.song.dto.SongRequestDTO;
import com.harmoniq.backend.song.dto.SongResponseDTO;
import com.harmoniq.backend.song.dto.SongUploadRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SongService {
    SongResponseDTO createSong(SongRequestDTO request);
    SongResponseDTO uploadSong(SongUploadRequestDTO request, MultipartFile audioFile);
    List<SongResponseDTO> getAllSongs();
    SongResponseDTO getSongById(Long songId);
    List<SongResponseDTO> getSongsByArtistId(Long artistId);
    SongResponseDTO updateMySong(Long songId, SongRequestDTO request);
    String deleteMySong(Long songId);

    Page<SongResponseDTO> searchSongs(String query, String genre, String sort, int page, int size);
}