package com.example.MovieService.controller;

import com.example.MovieService.entity.Movie;
import com.example.MovieService.entity.Showtime;
import com.example.MovieService.service.MovieService;
import com.example.MovieService.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.createMovie(movie));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<Map<String, Object>> getMovieByShowtimeId(@PathVariable Long showtimeId) {
        Optional<Showtime> showtimeOpt = showtimeService.getShowtimeById(showtimeId);

        if (!showtimeOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Showtime showtime = showtimeOpt.get();
        Optional<Movie> movieOpt = movieService.getMovieById(showtime.getMovie().getMovieId());

        if (!movieOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Movie movie = movieOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("movie", movie);
        response.put("showtime", showtime);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}