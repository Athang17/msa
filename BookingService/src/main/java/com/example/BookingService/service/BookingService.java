package com.example.BookingService.service;

import com.example.BookingService.entity.Booking;
import com.example.BookingService.entity.BookingDetail;
import com.example.BookingService.repository.BookingRepository;
import com.example.BookingService.repository.BookingDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    private static final BigDecimal PRICE_PER_TICKET = new BigDecimal(100);

    public Booking createBooking(Long userId, Long showtimeId, List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs cannot be null or empty.");
        }

        BigDecimal totalAmount = PRICE_PER_TICKET.multiply(BigDecimal.valueOf(seatIds.size()));

        Booking booking = new Booking(userId, showtimeId, seatIds.size(), totalAmount, seatIds);
        booking = bookingRepository.save(booking);

        for (Long seatId : seatIds) {
            BookingDetail bookingDetail = new BookingDetail(booking, seatId, PRICE_PER_TICKET);
            bookingDetailRepository.save(bookingDetail);
        }

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
