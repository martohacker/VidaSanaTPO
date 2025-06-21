package com.vidasana;

import java.util.Map;

public class DashboardDTO {
    private long totalPacientes;
    private long totalTurnosPendientes;
    private Map<String, Integer> pacientesPorMedico;
    private double promedioSueno;

    public DashboardDTO() {}

    public DashboardDTO(long totalPacientes, long totalTurnosPendientes, Map<String, Integer> pacientesPorMedico, double promedioSueno) {
        this.totalPacientes = totalPacientes;
        this.totalTurnosPendientes = totalTurnosPendientes;
        this.pacientesPorMedico = pacientesPorMedico;
        this.promedioSueno = promedioSueno;
    }

    public long getTotalPacientes() { return totalPacientes; }
    public void setTotalPacientes(long totalPacientes) { this.totalPacientes = totalPacientes; }
    public long getTotalTurnosPendientes() { return totalTurnosPendientes; }
    public void setTotalTurnosPendientes(long totalTurnosPendientes) { this.totalTurnosPendientes = totalTurnosPendientes; }
    public Map<String, Integer> getPacientesPorMedico() { return pacientesPorMedico; }
    public void setPacientesPorMedico(Map<String, Integer> pacientesPorMedico) { this.pacientesPorMedico = pacientesPorMedico; }
    public double getPromedioSueno() { return promedioSueno; }
    public void setPromedioSueno(double promedioSueno) { this.promedioSueno = promedioSueno; }
} 