package com.example.lab2projekt.domain.Objects.Entities;

import com.example.lab2projekt.domain.Objects.User.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String street; // Ulica

    @NotNull
    private String houseNumber; // Numer domu

    @NotNull
    private String city; // Miasto

    @NotNull
    private String postalCode; // Kod pocztowy

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order; // Relacja One-to-One z Order

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user; // Relacja Many-to-One z AppUser
}
