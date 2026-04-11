package com.harmoniq.backend.song.controller;

import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.song.entity.Song;
import com.harmoniq.backend.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SongStreamController {

    private final SongRepository songRepository;

    @GetMapping("/stream/{songId}")
    public ResponseEntity<Resource> streamSong(@PathVariable Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        String audioPath = song.getAudioUrl();
        File file = new File(audioPath);

        System.out.println("Song ID: " + songId);
        System.out.println("Audio path from DB: " + audioPath);
        System.out.println("Absolute path: " + file.getAbsolutePath());
        System.out.println("Exists: " + file.exists());

        if (!file.exists()) {
            throw new ResourceNotFoundException("Audio file not found on server");
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}