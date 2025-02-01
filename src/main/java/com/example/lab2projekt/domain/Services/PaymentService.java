package com.example.lab2projekt.domain.Services;


import com.example.lab2projekt.domain.Objects.Entities.Order;
import com.example.lab2projekt.domain.Objects.Entities.OrderStatus;
import com.example.lab2projekt.domain.Objects.Entities.Payment;
import com.example.lab2projekt.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void createPayment(String CreditCardNumber, Order order ){
        System.out.println("Nr kart kredytowej:" + CreditCardNumber);
        Payment payment = new Payment();
        payment.setCreditCardNumber(CreditCardNumber);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(OrderStatus.OPŁACONE);
        payment.setOrder(order);

        paymentRepository.save(payment);
    }

}

