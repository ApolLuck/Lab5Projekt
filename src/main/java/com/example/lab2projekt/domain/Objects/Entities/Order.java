package com.example.lab2projekt.domain.Objects.Entities;

import com.example.lab2projekt.domain.Objects.User.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Klucz główny

    @NotNull
    private OrderStatus orderStatus; // Status zamówienia (np. PENDING)

    @NotNull
    private BigDecimal totalPrice; // Całkowita cena zamówienia

    @NotNull
    private LocalDateTime orderDate; // Data złożenia zamówienia

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_address_id", referencedColumnName = "id")
    private Address deliveryAddress; // Relacja One-to-One z Address

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user; // Relacja Many-to-One z AppUser

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems; // Relacja One-to-Many z OrderItem

    @Email
    private String clientEmail;
}
