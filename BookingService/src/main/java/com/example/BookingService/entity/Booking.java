package com.example.BookingService.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long showtimeId;
    private int seats;
    private BigDecimal totalAmount;

    @Transient
    private List<Long> seatIds;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookingDetail> bookingDetails = new ArrayList<>();

    public Booking() {}

    public Booking(Long userId, Long showtimeId, int seats, BigDecimal totalAmount, List<Long> seatIds) {
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.seatIds = seatIds;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public List<BookingDetail> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingDetail> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public void addBookingDetail(BookingDetail detail) {
        bookingDetails.add(detail);
        detail.setBooking(this);
    }

    public void removeBookingDetail(BookingDetail detail) {
        bookingDetails.remove(detail);
        detail.setBooking(null);
    }
}