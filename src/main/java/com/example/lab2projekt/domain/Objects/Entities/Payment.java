package com.example.lab2projekt.domain.Objects.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

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
    @CreditCardNumber
    private String creditCardNumber;

    @NotNull
    private OrderStatus paymentStatus;

    @NotNull
    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order; // Relacja One-to-One z Order
}
