package com.pks.RESANALYZER.controller;

import com.pks.RESANALYZER.entity.Resume;
import com.pks.RESANALYZER.entity.User;
import com.pks.RESANALYZER.service.ResumeService;
import com.pks.RESANALYZER.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeRepository resumeRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
        if (user == null) {
            return ResponseEntity.status(401).body("User not authenticated.");
        }
        try {
            Resume newResume = resumeService.uploadResume(user, file);
            return ResponseEntity.ok(newResume);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getMyResumes(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        List<Resume> userResumes = resumeRepository.findByUser(user);
        return ResponseEntity.ok(userResumes);
    }
}