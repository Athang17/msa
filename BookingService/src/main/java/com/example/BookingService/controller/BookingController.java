package com.example.BookingService.controller;

import com.example.BookingService.entity.Booking;
import com.example.BookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PAYMENT_SERVICE_URL = "http://localhost:8082/api/payments";

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        if (booking.getUserId() == null || booking.getShowtimeId() == null || booking.getSeatIds() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Booking savedBooking = bookingService.createBooking(
                    booking.getUserId(),
                    booking.getShowtimeId(),
                    booking.getSeatIds());
            return ResponseEntity.ok(savedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/payment-status")
    public ResponseEntity<String> getBookingPaymentStatus(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            ResponseEntity<List> paymentsResponse = restTemplate.getForEntity(
                    PAYMENT_SERVICE_URL + "/booking/" + id,
                    List.class);

            List<Map<String, Object>> payments = paymentsResponse.getBody();
            if (payments == null || payments.isEmpty()) {
                return ResponseEntity.ok("NO_PAYMENT");
            }

            String status = (String) payments.get(0).get("paymentStatus");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.ok("ERROR");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/complete-payment")
    public ResponseEntity<String> completeBookingPayment(@PathVariable Long id,
            @RequestBody Map<String, String> paymentDetails) {
        try {
            Booking booking = bookingService.getBookingById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            ResponseEntity<List> paymentsResponse = restTemplate.getForEntity(
                    PAYMENT_SERVICE_URL + "/booking/" + id,
                    List.class);

            List<Map<String, Object>> payments = paymentsResponse.getBody();
            if (payments == null || payments.isEmpty()) {
                return ResponseEntity.badRequest().body("No payment found for this booking");
            }

            Map<String, Object> payment = payments.get(0);
            Integer paymentId = (Integer) payment.get("paymentId");

            restTemplate.put(
                    PAYMENT_SERVICE_URL + "/" + paymentId + "/status?status=SUCCESS",
                    null);

            return ResponseEntity.ok("Payment completed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to complete payment: " + e.getMessage());
        }
    }
}