package com.vidasana;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("medicos")
@Node("Paciente")
public class PacienteNeo4j {
    @Id
    private String id;
    private String nombre;

    @Relationship(type = "ATIENDE_A", direction = Relationship.Direction.INCOMING)
    private Set<MedicoNeo4j> medicos = new HashSet<>();

    public PacienteNeo4j() {}
    public PacienteNeo4j(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Set<MedicoNeo4j> getMedicos() { return medicos; }
    public void setMedicos(Set<MedicoNeo4j> medicos) { this.medicos = medicos; }
} 