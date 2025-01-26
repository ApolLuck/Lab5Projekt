package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {}
