package com.example.lab2projekt.domain.Objects.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="genres")
@Data
@NoArgsConstructor
public class PizzaGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String name;

    public PizzaGenre(String name){
        this.name = name;
    }
}

