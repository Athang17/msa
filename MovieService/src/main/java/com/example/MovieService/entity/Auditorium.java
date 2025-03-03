package com.example.MovieService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "auditoriums")
public class Auditorium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditorium_id;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(nullable = false)
    private String name;

    private int total_seats;

    public Auditorium() {}

    public Auditorium(Theater theater, String name, int total_seats) {
        this.theater = theater;
        this.name = name;
        this.total_seats = total_seats;
    }

    public Long getAuditorium_id() {
        return auditorium_id;
    }
    public void setAuditorium_id(Long auditorium_id) {
        this.auditorium_id = auditorium_id;
    }

    public Theater getTheater() {
        return theater;
    }
    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_seats() {
        return total_seats;
    }
    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }
}
