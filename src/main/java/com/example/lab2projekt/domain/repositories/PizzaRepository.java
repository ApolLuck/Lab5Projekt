package com.example.lab2projekt.domain.repositories;

import com.example.lab2projekt.domain.Objects.Entities.CoverType;
import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Objects.PizzaFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Integer>,
        JpaSpecificationExecutor<Pizza> {

    List<Pizza> findByNazwaContainingIgnoreCase(String nazwa);
    List<Pizza> findByNazwaContainingIgnoreCaseOrSkladnikiContainingIgnoreCase(String nazwa, String skladniki);
    List<Pizza> findByCenaBetween(Float minCena, Float maxCena);
    List<Pizza> findByCoverType(CoverType coverType);
    List<Pizza> findByPhraseInNameOrCoverType(String phrase);

    @Query("SELECT p FROM Pizza p " +
            "WHERE (:#{#filter.phrase} IS NULL OR " +
            "       UPPER(p.nazwa) LIKE CONCAT('%', UPPER(:#{#filter.phrase}), '%') OR " +
            "       UPPER(p.skladniki) LIKE CONCAT('%', UPPER(:#{#filter.phrase}), '%') OR " +
            "       UPPER(p.coverType.nazwa) LIKE CONCAT('%', UPPER(:#{#filter.phrase}), '%')) " +
            "AND (:#{#filter.cenaOd} IS NULL OR p.cena >= :#{#filter.cenaOd}) " +
            "AND (:#{#filter.cenaDo} IS NULL OR p.cena <= :#{#filter.cenaDo}) " +
            "AND (:#{#filter.dataOd} IS NULL OR p.data_wprowadzenia >= :#{#filter.dataOd}) " +
            "AND (:#{#filter.dataDo} IS NULL OR p.data_wprowadzenia <= :#{#filter.dataDo})")
    List<Pizza> findByFilter(PizzaFilter filter);

    List<Pizza> findAll(Specification<Pizza> spec);




}

// http://localhost:8080/filtered?phrase=pepperoni

//http://localhost:8080/filtered?cenaOd=30&cenaDo=50

//http://localhost:8080/filtered?dataOd=2024-01-01&dataDo=2024-11-05





