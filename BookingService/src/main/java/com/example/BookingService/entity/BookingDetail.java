package com.example.BookingService.entity;

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
    private Booking booking;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private BigDecimal price;

    public BookingDetail() {}

    public BookingDetail(Booking booking, Long seatId, BigDecimal price) {
        this.booking = booking;
        this.seatId = seatId;
        this.price = price;
    }

    public Long getBookingDetailId() {
        return bookingDetailId;
    }

    public Booking getBooking() {
        return booking;
    }

    public Long getSeatId() {
        return seatId;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
