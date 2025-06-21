package com.vidasana;

import java.util.ArrayList;
import java.util.List;

public class Historial {
    private Paciente paciente;
    private List<EntradaHistorial> entradas;

    public Historial(Paciente paciente) {
        this.paciente = paciente;
        this.entradas = new ArrayList<>();
    }

    public void agregarEntrada(Medico medico, String diagnostico) {
        entradas.add(new EntradaHistorial(medico, diagnostico));
    }
    public void eliminarEntrada(int indice) {
        if (indice >= 0 && indice < entradas.size()) {
            entradas.remove(indice);
        }
    }
    public List<EntradaHistorial> getEntradas() { return entradas; }
    @Override
    public String toString() {
        if (entradas.isEmpty()) {
            return "El historial está vacío.";
        }
        StringBuilder sb = new StringBuilder("Historial médico:\n");
        int i = 1;
        for (EntradaHistorial entrada : entradas) {
            sb.append(i++).append(". ").append(entrada).append("\n");
        }
        return sb.toString();
    }
}

 