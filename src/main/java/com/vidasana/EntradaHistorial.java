package com.vidasana;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

public class EntradaHistorial {
    private String id;
    private TipoEntrada tipo;
    private Medico medico;
    private LocalDateTime fecha;
    private String titulo;
    private String descripcion;
    private Map<String, Object> camposEspecificos;
    private String observaciones;

    public enum TipoEntrada {
        NOTA("Nota General"),
        DIAGNOSTICO("Diagnóstico"),
        RECETA("Receta Médica"),
        EXAMEN("Resultado de Examen"),
        TRATAMIENTO("Plan de Tratamiento"),
        ALERGIA("Información de Alergia"),
        VACUNA("Registro de Vacuna");

        private final String descripcion;

        TipoEntrada(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public EntradaHistorial() {
        this.fecha = LocalDateTime.now();
        this.camposEspecificos = new HashMap<>();
    }

    public EntradaHistorial(TipoEntrada tipo, Medico medico, String titulo, String descripcion) {
        this();
        this.tipo = tipo;
        this.medico = medico;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TipoEntrada getTipo() { return tipo; }
    public void setTipo(TipoEntrada tipo) { this.tipo = tipo; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Map<String, Object> getCamposEspecificos() { return camposEspecificos; }
    public void setCamposEspecificos(Map<String, Object> camposEspecificos) { this.camposEspecificos = camposEspecificos; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    // Métodos para manejar campos específicos
    public void agregarCampo(String clave, Object valor) {
        this.camposEspecificos.put(clave, valor);
    }

    public Object obtenerCampo(String clave) {
        return this.camposEspecificos.get(clave);
    }

    // Métodos específicos para cada tipo de entrada
    public void setMedicamentos(String medicamentos) {
        if (tipo == TipoEntrada.RECETA) {
            agregarCampo("medicamentos", medicamentos);
        }
    }

    public void setDosis(String dosis) {
        if (tipo == TipoEntrada.RECETA) {
            agregarCampo("dosis", dosis);
        }
    }

    public void setDuracion(String duracion) {
        if (tipo == TipoEntrada.TRATAMIENTO || tipo == TipoEntrada.RECETA) {
            agregarCampo("duracion", duracion);
        }
    }

    public void setResultado(String resultado) {
        if (tipo == TipoEntrada.EXAMEN) {
            agregarCampo("resultado", resultado);
        }
    }

    public void setAlergeno(String alergeno) {
        if (tipo == TipoEntrada.ALERGIA) {
            agregarCampo("alergeno", alergeno);
        }
    }

    public void setVacuna(String vacuna) {
        if (tipo == TipoEntrada.VACUNA) {
            agregarCampo("vacuna", vacuna);
        }
    }

    public void setLote(String lote) {
        if (tipo == TipoEntrada.VACUNA) {
            agregarCampo("lote", lote);
        }
    }

    @Override
    public String toString() {
        return "Médico: " + medico.getNombre() + " - Diagnóstico: " + descripcion;
    }
}

 