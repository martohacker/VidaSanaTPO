package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Paciente> getAll() {
        return pacienteRepository.findAll();
    }

    @PostMapping
    public Paciente create(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @GetMapping("/{id}")
    public Paciente getById(@PathVariable String id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Paciente update(@PathVariable String id, @RequestBody Paciente paciente) {
        paciente.setId(id);
        return pacienteRepository.save(paciente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        pacienteRepository.deleteById(id);
    }
} 