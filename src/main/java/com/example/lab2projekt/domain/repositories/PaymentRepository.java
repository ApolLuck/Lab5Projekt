package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.Entities.Order;
import com.example.lab2projekt.domain.Objects.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
