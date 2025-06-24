package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.*;

@Document(collection = "Pacientes")
public class Paciente {
    @Id
    private String id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @Min(value = 1, message = "El DNI debe ser mayor que 0")
    private int dni;
    @NotBlank(message = "La nacionalidad es obligatoria")
    private String nacionalidad;
    @Min(value = 1, message = "La edad debe ser mayor que 0")
    private int edad;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNac;
    @NotBlank(message = "El campo sueño es obligatorio")
    private String sueño;
    private String alimentacion;
    private String sintomas;
    
    @DBRef(lazy = true)
    private Historial historialMedico;
    
    @DBRef(lazy = true)
    private List<Turno> turnos = new ArrayList<>();
    
    @Field("habitosYSintomas")
    private List<HabitoSintoma> habitosYSintomas = new ArrayList<>();

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
    public void setSueño(String sueño) { this.sueño = sueño; }
    public String getAlimentacion() { return alimentacion; }
    public void setAlimentacion(String alimentacion) { this.alimentacion = alimentacion; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public List<Turno> getTurnos() { return turnos; }
    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    public void setHistorialMedico(Historial historialMedico) { this.historialMedico = historialMedico; }
    public List<HabitoSintoma> getHabitosYSintomas() { return habitosYSintomas; }
    public void setHabitosYSintomas(List<HabitoSintoma> habitosYSintomas) { this.habitosYSintomas = habitosYSintomas; }
}

 