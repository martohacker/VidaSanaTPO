package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Pacientes")
public class Paciente {
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private int dni;
    private String nacionalidad;
    private int edad;
    private LocalDate fechaNac;
    private String sueño;
    private String alimentacion;
    private String sintomas;
    private Historial historialMedico;
    private List<Turno> turnos = new ArrayList<>();

    public Paciente() {
        // Constructor vacío requerido por Spring Data
    }

    public Paciente(String nombre, String apellido, String email, int dni, String nacionalidad, int edad, LocalDate fechaNac) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.nacionalidad = nacionalidad;
        this.edad = edad;
        this.fechaNac = fechaNac;
        this.historialMedico = new Historial(this);
    }

    public void registrarTurno(Turno turno) { turnos.add(turno); }
    public void cancelarTurno(Turno turno) { turnos.remove(turno); }
    public List<Turno> consultarTurnos() { return turnos; }
    public Historial getHistorialMedico() { return historialMedico; }
    public int getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getNacionalidad() { return nacionalidad; }
    public int getEdad() { return edad; }
    public LocalDate getFechaNac() { return fechaNac; }
    public String getSueño() { return sueño; }
    public String getAlimentacion() { return alimentacion; }
    public String getSintomas() { return sintomas; }
    public List<Turno> getTurnos() { return turnos; }
    public void setId(String id) { this.id = id; }
}

 