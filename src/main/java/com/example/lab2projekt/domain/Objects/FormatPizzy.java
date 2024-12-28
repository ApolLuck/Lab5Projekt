package com.example.lab2projekt.domain.Objects;

import java.io.Serializable;

public class FormatPizzy implements Serializable {
    private int szerokosc;
    private int grubosc;

    public FormatPizzy() {}

    public FormatPizzy(int szerokosc, int grubosc) {
        this.szerokosc = szerokosc;
        this.grubosc = grubosc;
    }

    // Metody dostępowe
    public int getSzerokosc() {
        return szerokosc;
    }

    public void setSzerokosc(int szerokosc) {
        this.szerokosc = szerokosc;
    }

    public int getGrubosc() {
        return grubosc;
    }

    public void setGrubosc(int grubosc) {
        this.grubosc = grubosc;
    }

    @Override
    public String toString() {
        return szerokosc + "x" + grubosc;
    }
}
