package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "Historiales")
public class Historial {
    @Id
    private String id;
    
    @DBRef(lazy = true)
    private Paciente paciente;
    
    private List<EntradaHistorial> entradas;

    public Historial() {
        this.entradas = new ArrayList<>();
    }

    public Historial(Paciente paciente) {
        this.paciente = paciente;
        this.entradas = new ArrayList<>();
    }

    // Método general para agregar cualquier tipo de entrada
    public void agregarEntrada(EntradaHistorial entrada) {
        entradas.add(entrada);
    }

    // Métodos específicos para cada tipo de entrada
    public void agregarNota(Medico medico, String titulo, String descripcion) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.NOTA, medico, titulo, descripcion
        );
        entradas.add(entrada);
    }

    public void agregarDiagnostico(Medico medico, String titulo, String diagnostico) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.DIAGNOSTICO, medico, titulo, diagnostico
        );
        entradas.add(entrada);
    }

    public void agregarReceta(Medico medico, String titulo, String descripcion, 
                             String medicamentos, String dosis, String duracion) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.RECETA, medico, titulo, descripcion
        );
        entrada.setMedicamentos(medicamentos);
        entrada.setDosis(dosis);
        entrada.setDuracion(duracion);
        entradas.add(entrada);
    }

    public void agregarExamen(Medico medico, String titulo, String descripcion, String resultado) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.EXAMEN, medico, titulo, descripcion
        );
        entrada.setResultado(resultado);
        entradas.add(entrada);
    }

    public void agregarTratamiento(Medico medico, String titulo, String descripcion, String duracion) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.TRATAMIENTO, medico, titulo, descripcion
        );
        entrada.setDuracion(duracion);
        entradas.add(entrada);
    }

    public void agregarAlergia(Medico medico, String titulo, String descripcion, String alergeno) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.ALERGIA, medico, titulo, descripcion
        );
        entrada.setAlergeno(alergeno);
        entradas.add(entrada);
    }

    public void agregarVacuna(Medico medico, String titulo, String descripcion, 
                             String vacuna, String lote) {
        EntradaHistorial entrada = new EntradaHistorial(
            EntradaHistorial.TipoEntrada.VACUNA, medico, titulo, descripcion
        );
        entrada.setVacuna(vacuna);
        entrada.setLote(lote);
        entradas.add(entrada);
    }

    // Métodos para consultar entradas por tipo
    public List<EntradaHistorial> getEntradasPorTipo(EntradaHistorial.TipoEntrada tipo) {
        return entradas.stream()
                .filter(entrada -> entrada.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    public List<EntradaHistorial> getDiagnosticos() {
        return getEntradasPorTipo(EntradaHistorial.TipoEntrada.DIAGNOSTICO);
    }

    public List<EntradaHistorial> getRecetas() {
        return getEntradasPorTipo(EntradaHistorial.TipoEntrada.RECETA);
    }

    public List<EntradaHistorial> getExamenes() {
        return getEntradasPorTipo(EntradaHistorial.TipoEntrada.EXAMEN);
    }

    public List<EntradaHistorial> getAlergias() {
        return getEntradasPorTipo(EntradaHistorial.TipoEntrada.ALERGIA);
    }

    public List<EntradaHistorial> getVacunas() {
        return getEntradasPorTipo(EntradaHistorial.TipoEntrada.VACUNA);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public List<EntradaHistorial> getEntradas() { return entradas; }
    public void setEntradas(List<EntradaHistorial> entradas) { this.entradas = entradas; }

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

 