package com.example.lab2projekt.domain.Objects.Entities;

public enum PizzaSize {
    FAMILIJNA(42),
    DUZA(32),
    MALA(26);

    private final int size;

    PizzaSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
