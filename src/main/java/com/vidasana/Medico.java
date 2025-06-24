package com.vidasana;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Profesionales")
public class Medico {
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String mail;
    private Integer dni;
    private int edad;
    private LocalDate fechaNac;
    private String disciplina;
    private List<Paciente> pacientes;
    private List<Turno> turnos;

    public Medico() {
        // Constructor vacío requerido por Spring Data
    }

    public Medico(String nombre, String apellido, String mail, Integer dni, int edad, LocalDate fechaNac, String disciplina) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.dni = dni;
        this.edad = edad;
        this.fechaNac = fechaNac;
        this.disciplina = disciplina;
        this.pacientes = new ArrayList<>();
        this.turnos = new ArrayList<>();
    }

    public List<Turno> verTurnos() { return turnos; }
    public void agregarTurno(Turno turno) { turnos.add(turno); }
    public void modificarHistorial(Paciente paciente, String diagnostico) { 
        paciente.getHistorialMedico().agregarDiagnostico(this, "Diagnóstico", diagnostico); 
    }
    public Historial verHistorial(Paciente paciente) { return paciente.getHistorialMedico(); }
    public void removerTurnosDePaciente(Paciente p) {
        turnos.removeIf(t -> t.getPacienteId().equals(String.valueOf(p.getDni())));
    }
    public List<Paciente> getPacientes() { return pacientes; }
    public void removerTurno(Turno t) { turnos.remove(t); }
    public Integer getDni() { return dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public void setDni(Integer dni) { this.dni = dni; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public LocalDate getFechaNac() { return fechaNac; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }
    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }
    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }
    public List<Turno> getTurnos() { return turnos; }
    public void setTurnos(List<Turno> turnos) { this.turnos = turnos; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}

 