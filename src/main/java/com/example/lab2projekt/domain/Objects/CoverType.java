package com.example.lab2projekt.domain.Objects;

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
@Table(name = "cover_types")
public class CoverType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String nazwa;
    @NotNull
    private String material;

    public CoverType(String nazwa, String material) {
        this.nazwa = nazwa;
        this.material = material;
    }

}
