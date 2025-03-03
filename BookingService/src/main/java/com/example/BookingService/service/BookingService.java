package com.example.BookingService.service;

import com.example.BookingService.entity.Booking;
import com.example.BookingService.entity.BookingDetail;
import com.example.BookingService.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    private static final BigDecimal PRICE_PER_TICKET = new BigDecimal(100);

    public Booking createBooking(Long userId, Long showtimeId, List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs cannot be null or empty.");
        }

        BigDecimal totalAmount = PRICE_PER_TICKET.multiply(BigDecimal.valueOf(seatIds.size()));

        Booking booking = new Booking(userId, showtimeId, seatIds.size(), totalAmount, seatIds);
        // Add BookingDetail entries via helper method
        for (Long seatId : seatIds) {
            BookingDetail detail = new BookingDetail();
            detail.setSeatId(seatId);
            detail.setPrice(PRICE_PER_TICKET);
            booking.addBookingDetail(detail);
        }

        // Cascade persist will handle saving BookingDetail objects
        booking = bookingRepository.save(booking);
        return booking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }
}
