package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/neo4j")
public class Neo4jRedController {
    @Autowired
    private MedicoNeoRepository medicoRepo;
    @Autowired
    private PacienteNeoRepository pacienteRepo;

    @PostMapping("/medico")
    public MedicoNeo4j createMedico(@RequestBody MedicoNeo4j medico) {
        return medicoRepo.save(medico);
    }

    @PostMapping("/paciente")
    public PacienteNeo4j createPaciente(@RequestBody PacienteNeo4j paciente) {
        return pacienteRepo.save(paciente);
    }

    @PostMapping("/asignar")
    public String asignar(@RequestParam String medicoId, @RequestParam String pacienteId) {
        MedicoNeo4j medico = medicoRepo.findById(medicoId).orElseThrow();
        PacienteNeo4j paciente = pacienteRepo.findById(pacienteId).orElseThrow();
        medico.getPacientes().add(paciente);
        medicoRepo.save(medico);
        return "Relación creada: " + medico.getNombre() + " atiende a " + paciente.getNombre();
    }

    @GetMapping("/medico/{id}/pacientes")
    public Set<PacienteNeo4j> getPacientes(@PathVariable String id) {
        return medicoRepo.findById(id).orElseThrow().getPacientes();
    }

    @GetMapping("/paciente/{id}/medicos")
    public Set<MedicoNeo4j> getMedicos(@PathVariable String id) {
        return pacienteRepo.findById(id).orElseThrow().getMedicos();
    }
} 