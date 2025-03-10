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

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_GATEWAY_URL = "http://APIGateway/api";
    private static final String MOVIE_SERVICE_URL = API_GATEWAY_URL + "/movies";
    private static final String USER_SERVICE_URL = API_GATEWAY_URL + "/users";
    private static final String PAYMENT_SERVICE_URL = API_GATEWAY_URL + "/payments";

    private static final BigDecimal PRICE_PER_TICKET = new BigDecimal(100);

    public Booking createBooking(Long userId, Long showtimeId, List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs cannot be null or empty.");
        }

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

        Map<String, Object> user = restTemplate.getForObject(
                USER_SERVICE_URL + "/" + userId,
                Map.class);

        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        BigDecimal totalAmount = PRICE_PER_TICKET.multiply(BigDecimal.valueOf(seatIds.size()));

        Booking booking = new Booking(userId, showtimeId, seatIds.size(), totalAmount, seatIds);
        for (Long seatId : seatIds) {
            BookingDetail detail = new BookingDetail();
            detail.setSeatId(seatId);
            detail.setPrice(PRICE_PER_TICKET);
            booking.addBookingDetail(detail);
        }

        booking = bookingRepository.save(booking);

        Map<String, Integer> updateSeats = new HashMap<>();
        updateSeats.put("available_seats", availableSeats - seatIds.size());
        restTemplate.put(MOVIE_SERVICE_URL + "/showtimes/" + showtimeId + "/seats", updateSeats);

        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("bookingId", booking.getId());
        paymentRequest.put("paymentMethod", "CARD"); // Default payment method
        paymentRequest.put("amount", totalAmount.doubleValue());

        restTemplate.postForObject(PAYMENT_SERVICE_URL + "/create", paymentRequest, Map.class);

        return booking;
    }

    public List<Booking> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            return bookings;
        } catch (Exception e) {
            System.err.println("Error retrieving bookings: " + e.getMessage());
            throw e;
        }
    }

    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public void deleteBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            Booking bookingData = booking.get();

            try {
                restTemplate.delete(PAYMENT_SERVICE_URL + "/booking/" + bookingId);
            } catch (Exception e) {
                System.err.println("Error cancelling payment: " + e.getMessage());
            }

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
                System.err.println("Error updating showtime: " + e.getMessage());
            }
        }

        bookingRepository.deleteById(bookingId);
    }
}