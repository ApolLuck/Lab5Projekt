package com.example.lab2projekt.domain.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PizzaFilter {
    private String phrase;
    private Double cenaOd;
    private Double cenaDo;
    private LocalDate dataOd;
    private LocalDate dataDo;

}
