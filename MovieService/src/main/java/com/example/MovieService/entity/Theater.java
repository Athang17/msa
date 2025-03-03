package com.example.MovieService.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "theaters")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theater_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    private int total_auditoriums;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Auditorium> auditoriums;

    public Theater() {}

    public Theater(String name, String location, int total_auditoriums) {
        this.name = name;
        this.location = location;
        this.total_auditoriums = total_auditoriums;
    }

    public Long getTheater_id() {
        return theater_id;
    }
    public void setTheater_id(Long theater_id) {
        this.theater_id = theater_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotal_auditoriums() {
        return total_auditoriums;
    }
    public void setTotal_auditoriums(int total_auditoriums) {
        this.total_auditoriums = total_auditoriums;
    }
}
