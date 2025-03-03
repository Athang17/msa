package com.example.MovieService.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String genre;
    private String description;
    private int duration;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String language;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
            name = "movie_showtimes",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "showtime")
    private List<LocalDateTime> showtimes = new ArrayList<>();

    private String theater;
    private String auditorium;

    public Movie() {}

    public Movie(String title, String genre, String description, int duration,
                 LocalDate releaseDate, String language, List<LocalDateTime> showtimes,
                 String theater, String auditorium) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.language = language;
        this.showtimes = showtimes;
        this.theater = theater;
        this.auditorium = auditorium;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<LocalDateTime> getShowtimes() {
        return showtimes;
    }
    public void setShowtimes(List<LocalDateTime> showtimes) {
        this.showtimes = showtimes;
    }

    public String getTheater() {
        return theater;
    }
    public void setTheater(String theater) {
        this.theater = theater;
    }

    public String getAuditorium() {
        return auditorium;
    }
    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }
}