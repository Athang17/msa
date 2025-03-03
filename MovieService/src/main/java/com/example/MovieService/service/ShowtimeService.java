package com.example.MovieService.service;

import com.example.MovieService.entity.Showtime;
import com.example.MovieService.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    public Showtime createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public List<Showtime> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovie_MovieId(movieId);
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        return showtimeRepository.findById(id).map(showtime -> {
            showtime.setShow_date(showtimeDetails.getShow_date());
            showtime.setShow_time(showtimeDetails.getShow_time());
            showtime.setPrice(showtimeDetails.getPrice());
            showtime.setAvailable_seats(showtimeDetails.getAvailable_seats());
            return showtimeRepository.save(showtime);
        }).orElseThrow(() -> new RuntimeException("Showtime not found with id: " + id));
    }

    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }
}
