package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserSessionCookie(String userSessionCookie);
    List<Order> findByClientEmailContainingIgnoreCaseAndId(String clientEmail, Long id);

}
