package com.example.MovieService.service;

import com.example.MovieService.entity.Theater;
import com.example.MovieService.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;

    public Theater createTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    public Theater updateTheater(Long id, Theater theaterDetails) {
        return theaterRepository.findById(id).map(theater -> {
            theater.setName(theaterDetails.getName());
            theater.setLocation(theaterDetails.getLocation());
            theater.setTotal_auditoriums(theaterDetails.getTotal_auditoriums());
            return theaterRepository.save(theater);
        }).orElseThrow(() -> new RuntimeException("Theater not found with id: " + id));
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }
}
