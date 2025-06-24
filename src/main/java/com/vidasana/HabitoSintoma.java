package com.vidasana;

import java.time.LocalDate;

public class HabitoSintoma {
    private LocalDate fecha;
    private String descripcion;

    public HabitoSintoma() {}
    public HabitoSintoma(LocalDate fecha, String descripcion) {
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
} 