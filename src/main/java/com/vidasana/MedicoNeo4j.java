package com.vidasana;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("pacientes")
@Node("Medico")
public class MedicoNeo4j {
    @Id
    private String id;
    private String nombre;

    @Relationship(type = "ATIENDE_A")
    private Set<PacienteNeo4j> pacientes = new HashSet<>();

    public MedicoNeo4j() {}
    public MedicoNeo4j(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Set<PacienteNeo4j> getPacientes() { return pacientes; }
    public void setPacientes(Set<PacienteNeo4j> pacientes) { this.pacientes = pacientes; }
} 