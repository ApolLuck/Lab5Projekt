package com.example.lab2projekt.domain.Objects;

import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PizzaSpecifications {
    public static Specification<Pizza> findByPhrase(final String phrase) {
        return (pizza, query, cb) -> {
            if(!StringUtils.isEmpty(phrase)){
                String phraseLike = "%"+phrase.toUpperCase() +"%";
                return cb.or(
                        cb.like(cb.upper(pizza.get("nazwa")), phraseLike),
                        cb.like(cb.upper(pizza.get("skladniki")), phraseLike),
                        cb.like(cb.upper(pizza.get("coverType").get("nazwa")), phraseLike)
                );
            }return null;
        };
    }

    public static Specification<Pizza> findByPriceRange(Double minPrice, Double maxPrice) {
        return (pizza, query, cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(pizza.get("cena"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(pizza.get("cena"), minPrice);
            } else if (maxPrice != null) {
                return cb.lessThanOrEqualTo(pizza.get("cena"), maxPrice);
            }
            return null;
        };
    }

    public static Specification<Pizza> findByDateRange(LocalDate minDate, LocalDate maxDate) {
        return (pizza, query, cb) -> {
            if (minDate != null && maxDate != null) {
                return cb.between(pizza.get("data_wprowadzenia"), minDate, maxDate);
            } else if (minDate != null) {
                return cb.greaterThanOrEqualTo(pizza.get("data_wprowadzenia"), minDate);
            } else if (maxDate != null) {
                return cb.lessThanOrEqualTo(pizza.get("data_wprowadzenia"), maxDate);
            }
            return null;
        };
    }





}
