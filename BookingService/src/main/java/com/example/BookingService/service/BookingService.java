package com.example.BookingService.service;

import com.example.BookingService.entity.Booking;
import com.example.BookingService.entity.BookingDetail;
import com.example.BookingService.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String MOVIE_SERVICE_URL = "http://localhost:8080/api";
    private static final String USER_SERVICE_URL = "http://localhost:8083/api/users";
    private static final String PAYMENT_SERVICE_URL = "http://localhost:8082/api/payments";

    private static final BigDecimal PRICE_PER_TICKET = new BigDecimal(100);

    public Booking createBooking(Long userId, Long showtimeId, List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs cannot be null or empty.");
        }

        // Verify showtime exists and has available seats
        Map<String, Object> showtime = restTemplate.getForObject(
                MOVIE_SERVICE_URL + "/showtimes/" + showtimeId,
                Map.class);

        if (showtime == null) {
            throw new RuntimeException("Showtime not found with id: " + showtimeId);
        }

        Integer availableSeats = (Integer) showtime.get("available_seats");
        if (availableSeats < seatIds.size()) {
            throw new RuntimeException("Not enough available seats for this showtime");
        }

        // Verify user exists
        Map<String, Object> user = restTemplate.getForObject(
                USER_SERVICE_URL + "/" + userId,
                Map.class);

        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
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

        // Save booking
        booking = bookingRepository.save(booking);

        // Update available seats in showtime
        Map<String, Integer> updateSeats = new HashMap<>();
        updateSeats.put("available_seats", availableSeats - seatIds.size());
        restTemplate.put(MOVIE_SERVICE_URL + "/showtimes/" + showtimeId + "/seats", updateSeats);

        // Create payment request
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("bookingId", booking.getId());
        paymentRequest.put("paymentMethod", "CARD"); // Default payment method
        paymentRequest.put("amount", totalAmount.doubleValue());

        // Call payment service to initialize payment
        restTemplate.postForObject(PAYMENT_SERVICE_URL + "/create", paymentRequest, Map.class);

        return booking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public void deleteBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            Booking bookingData = booking.get();

            // Call payment service to cancel any pending payments
            try {
                restTemplate.delete(PAYMENT_SERVICE_URL + "/booking/" + bookingId);
            } catch (Exception e) {
                // Log error but continue with deletion
                System.err.println("Error cancelling payment: " + e.getMessage());
            }

            // Free up the seats in the showtime
            try {
                Map<String, Object> showtime = restTemplate.getForObject(
                        MOVIE_SERVICE_URL + "/showtimes/" + bookingData.getShowtimeId(),
                        Map.class);

                if (showtime != null) {
                    Integer availableSeats = (Integer) showtime.get("available_seats");
                    Map<String, Integer> updateSeats = new HashMap<>();
                    updateSeats.put("available_seats", availableSeats + bookingData.getSeats());
                    restTemplate.put(MOVIE_SERVICE_URL + "/showtimes/" + bookingData.getShowtimeId() + "/seats",
                            updateSeats);
                }
            } catch (Exception e) {
                // Log error but continue with deletion
                System.err.println("Error updating showtime: " + e.getMessage());
            }
        }

        bookingRepository.deleteById(bookingId);
    }
}