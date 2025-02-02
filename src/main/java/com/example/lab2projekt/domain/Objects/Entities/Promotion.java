package com.example.lab2projekt.domain.Objects.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    private Double discountPercentage;
    private String promotionCode;
    private LocalDate validFrom;
    private LocalDate validTo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreatedBy
    private String createdBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @LastModifiedBy
    private String modifiedBy;
    @ManyToMany
    @JoinTable(
            name = "promotion_pizzas",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private Set<Pizza> pizzas;

    public Promotion(String title, String description, Double discountPercentage, String promotionCode,
                     LocalDate validFrom, LocalDate validTo, Set<Pizza> pizzas) {
        this.title = title;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.promotionCode =  promotionCode;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.pizzas = pizzas;
    }

}
