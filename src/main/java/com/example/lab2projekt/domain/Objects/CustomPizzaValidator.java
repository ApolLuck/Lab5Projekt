package com.example.lab2projekt.domain.Objects;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomPizzaValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Pizza.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Pizza pizza = (Pizza) o;

        if (pizza.getNazwa().equals(pizza.getSkladniki())) {
            errors.rejectValue("skladniki", "taSamaNazwa.pizza.skladniki",
                    "Skladniki nie mogą być identyczne jak nazwa");
        }

    }
}

