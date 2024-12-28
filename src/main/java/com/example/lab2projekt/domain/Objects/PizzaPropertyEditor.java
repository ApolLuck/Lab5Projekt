package com.example.lab2projekt.domain.Objects;

import java.beans.PropertyEditorSupport;

public class PizzaPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String[] parts = text.split("x");
        if (parts.length == 2) {
            try {
                int szerokosc = Integer.parseInt(parts[0].trim());
                int grubosc = Integer.parseInt(parts[1].trim());
                setValue(new FormatPizzy(szerokosc, grubosc));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("nieprawidłowy format, oczekiwany to szerokosc x grubosc");
            }
        } else {
            throw new IllegalArgumentException("nieprawidłowy format, oczekiwany to szerokosc x grubosc");
        }
    }

    @Override
    public String getAsText() {
        FormatPizzy formatPizzy = (FormatPizzy) getValue();
        return formatPizzy != null ? formatPizzy.toString() : "";
    }
}

