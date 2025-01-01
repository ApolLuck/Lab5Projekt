package com.example.lab2projekt.domain.Objects;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class FormatPizzy implements Serializable {
    private int szerokosc;
    private int grubosc;

    public FormatPizzy() {}

    public FormatPizzy(int szerokosc, int grubosc) {
        this.szerokosc = szerokosc;
        this.grubosc = grubosc;
    }

    @Override
    public String toString() {
        return szerokosc + "x" + grubosc;
    }
}
