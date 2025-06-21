package com.vidasana;

import java.time.LocalDate;

public class Especialista extends Medico {
    private String especialidad;

    public Especialista(String nombre, String apellido, String mail, int dni, int edad, LocalDate fechaNac, String disciplina, String especialidad) {
        super(nombre, apellido, mail, dni, edad, fechaNac, disciplina);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
}

 