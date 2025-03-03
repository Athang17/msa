package com.example.MovieService.service;

import com.example.MovieService.entity.Auditorium;
import com.example.MovieService.repository.AuditoriumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuditoriumService {
    @Autowired
    private AuditoriumRepository auditoriumRepository;

    public Auditorium createAuditorium(Auditorium auditorium) {
        return auditoriumRepository.save(auditorium);
    }

    public List<Auditorium> getAllAuditoriums() {
        return auditoriumRepository.findAll();
    }

    public Optional<Auditorium> getAuditoriumById(Long id) {
        return auditoriumRepository.findById(id);
    }

    public Auditorium updateAuditorium(Long id, Auditorium auditoriumDetails) {
        return auditoriumRepository.findById(id).map(auditorium -> {
            auditorium.setName(auditoriumDetails.getName());
            auditorium.setTotal_seats(auditoriumDetails.getTotal_seats());
            return auditoriumRepository.save(auditorium);
        }).orElseThrow(() -> new RuntimeException("Auditorium not found with id: " + id));
    }

    public void deleteAuditorium(Long id) {
        auditoriumRepository.deleteById(id);
    }
}
