package com.example.lab2projekt.domain.Objects.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Klucz główny

    @NotNull
    private Integer quantity; // Ilość zamówionej pizzy

    @NotNull
    private BigDecimal price; // Cena za ten element zamówienia

    @ManyToOne
    @JoinColumn(name = "pizza_id", referencedColumnName = "id")
    private Pizza pizza; // Relacja Many-to-One z Pizza

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order; // Relacja Many-to-One z Order
}
