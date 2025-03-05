package com.example.PaymentService.service;

import com.example.PaymentService.entity.Payment;
import com.example.PaymentService.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Long bookingId, String paymentMethod, Double amount) {
        String transactionId = UUID.randomUUID().toString();
        Payment payment = new Payment(bookingId, paymentMethod, amount, transactionId, "PENDING");
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getBookingId().equals(bookingId))
                .collect(Collectors.toList());
    }

    public Payment updatePaymentStatus(Long paymentId, String status) {
        return paymentRepository.findById(paymentId).map(payment -> {
            payment.setPaymentStatus(status);
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    public void deletePaymentsByBookingId(Long bookingId) {
        List<Payment> payments = getPaymentsByBookingId(bookingId);
        for (Payment payment : payments) {
            paymentRepository.deleteById(payment.getPaymentId());
        }
    }
}