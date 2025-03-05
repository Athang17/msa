package com.example.MovieService.repository;

import com.example.MovieService.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovie_MovieId(Long movieId);
}