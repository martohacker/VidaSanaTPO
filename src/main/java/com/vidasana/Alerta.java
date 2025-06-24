package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDateTime;

@Document(collection = "Alertas")
public class Alerta {
    @Id
    private String id;
    @DBRef(lazy = true)
    private Paciente paciente;
    private LocalDateTime fecha;
    private TipoAlerta tipo;
    private String descripcion;
    private EstadoAlerta estado;

    public enum TipoAlerta {
        RIESGO, SINTOMA, HABITO
    }
    public enum EstadoAlerta {
        PENDIENTE, RESUELTA
    }

    public Alerta() {}
    public Alerta(Paciente paciente, TipoAlerta tipo, String descripcion) {
        this.paciente = paciente;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoAlerta.PENDIENTE;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public TipoAlerta getTipo() { return tipo; }
    public void setTipo(TipoAlerta tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EstadoAlerta getEstado() { return estado; }
    public void setEstado(EstadoAlerta estado) { this.estado = estado; }
} 