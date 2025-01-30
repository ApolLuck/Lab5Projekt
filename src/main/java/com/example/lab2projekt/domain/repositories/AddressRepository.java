package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.Entities.Address;
import com.example.lab2projekt.domain.Objects.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}