package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Turnos")
public class Turno {
    @Id
    private String id;
    private String pacienteId;
    private String medicoId;
    private String fechaHora;
    private String estado;

    public Turno() {}

    public Turno(String pacienteId, String medicoId, String fechaHora, String estado) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }
    public String getMedicoId() { return medicoId; }
    public void setMedicoId(String medicoId) { this.medicoId = medicoId; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

 