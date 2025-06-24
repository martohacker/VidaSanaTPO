package com.vidasana;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    
    @Field("username")
    private String username;
    
    @Field("password")
    private String password;
    
    @Field("rol")
    private String rol; // "ADMIN", "MEDICO", "PACIENTE"
    
    @Field("pacienteId")
    private String pacienteId; // Solo si rol es PACIENTE
    
    @Field("medicoId")
    private String medicoId; // Solo si rol es MEDICO
    
    @Field("nombreCompleto")
    private String nombreCompleto;

    public Usuario() {}

    public Usuario(String username, String password, String rol, String nombreCompleto) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }
    
    public String getMedicoId() { return medicoId; }
    public void setMedicoId(String medicoId) { this.medicoId = medicoId; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
} 