package com.example.lab2projekt.domain.Objects.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@NamedQuery(name = "Pizza.findByPhraseInNameOrCoverType",
        query = "select p from Pizza p " +
                "where upper(p.nazwa) like upper(?1) " +            // gdzie ?1 to pierwszy parametr wywołania
                "or upper(p.coverType.nazwa) like upper(?1) ")      // a nie nr kolumny tak jak w SQL
@Table(name = "pizzas")
public class Pizza {

    public Pizza(String nazwa, String skladniki, PizzaSize rozmiar, float cena, boolean grube_ciasto,
                 boolean dodatkowy_ser, LocalDate data_wprowadzenia, CoverType coverType) {
        this.nazwa = nazwa;
        this.skladniki = skladniki;
        this.rozmiar = rozmiar;
        this.cena = cena;
        this.grube_ciasto = grube_ciasto;
        this.dodatkowy_ser = dodatkowy_ser;
        this.data_wprowadzenia = data_wprowadzenia;
        this.coverType = coverType;
        this.genres = new HashSet<>();//Inicjalizacja zbioru
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty
    private String nazwa;
    @NotEmpty
    private String skladniki;
    @Enumerated(EnumType.STRING)
    @NotNull
    private PizzaSize rozmiar;
    @Min(30) @Max(100)
    @NumberFormat(pattern = "#.00")
    private float cena;
    private boolean grube_ciasto;
    private boolean dodatkowy_ser;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_wprowadzenia;
    @NotNull
    @ManyToOne // Pizza może mieć jeden typ opakowania
    @JoinColumn(name = "cover_type_id")
    private CoverType coverType;
    @NotEmpty
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<PizzaGenre> genres;
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
    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] fileContent;
    private String fileName;

}


