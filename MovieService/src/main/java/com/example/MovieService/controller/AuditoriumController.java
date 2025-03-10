package com.example.MovieService.controller;

import com.example.MovieService.entity.Auditorium;
import com.example.MovieService.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auditoriums")
public class AuditoriumController {
    @Autowired
    private AuditoriumService auditoriumService;

    @PostMapping
    public ResponseEntity<Auditorium> createAuditorium(@RequestBody Auditorium auditorium) {
        return ResponseEntity.ok(auditoriumService.createAuditorium(auditorium));
    }

    @GetMapping
    public ResponseEntity<List<Auditorium>> getAllAuditoriums() {
        return ResponseEntity.ok(auditoriumService.getAllAuditoriums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditorium> getAuditoriumById(@PathVariable Long id) {
        Optional<Auditorium> auditorium = auditoriumService.getAuditoriumById(id);
        return auditorium.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auditorium> updateAuditorium(@PathVariable Long id, @RequestBody Auditorium auditoriumDetails) {
        return ResponseEntity.ok(auditoriumService.updateAuditorium(id, auditoriumDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditorium(@PathVariable Long id) {
        auditoriumService.deleteAuditorium(id);
        return ResponseEntity.noContent().build();
    }
}
