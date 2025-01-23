package com.example.lab2projekt.domain.Objects.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String paymentType; // Typ płatności (np. KARTA, PAYPAL)

    @NotNull
    private String paymentStatus; // Status płatności (np. COMPLETED)

    @NotNull
    private LocalDateTime paymentDate; // Data płatności

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order; // Relacja One-to-One z Order
}
