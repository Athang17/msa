package com.example.BookingService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "booking_details")
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingDetailId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference
    private Booking booking;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private BigDecimal price;

    // Constructors
    public BookingDetail() {}

    public BookingDetail(Booking booking, Long seatId, BigDecimal price) {
        this.booking = booking;
        this.seatId = seatId;
        this.price = price;
    }

    // Getters & Setters
    public Long getBookingDetailId() {
        return bookingDetailId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
