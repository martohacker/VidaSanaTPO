package com.vidasana;

public class EntradaHistorial {
    private Medico medico;
    private String diagnostico;

    public EntradaHistorial(Medico medico, String diagnostico) {
        this.medico = medico;
        this.diagnostico = diagnostico;
    }

    public Medico getMedico() { return medico; }
    public String getDiagnostico() { return diagnostico; }
    @Override
    public String toString() {
        return "Médico: " + medico.getNombre() + " - Diagnóstico: " + diagnostico;
    }
}

 