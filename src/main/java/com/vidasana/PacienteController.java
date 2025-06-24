package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

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
    public Paciente create(@RequestBody @Valid Paciente paciente) {
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

    // Obtener historial médico de un paciente
    @GetMapping("/{id}/historial")
    public List<EntradaHistorial> getHistorial(@PathVariable String id) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null && paciente.getHistorialMedico() != null) {
            return paciente.getHistorialMedico().getEntradas();
        }
        return List.of();
    }

    // Agregar entrada al historial médico
    @PostMapping("/{id}/historial")
    public List<EntradaHistorial> addEntradaHistorial(@PathVariable String id, @RequestBody EntradaHistorial entrada) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null) {
            if (paciente.getHistorialMedico() == null) {
                paciente.setHistorialMedico(new Historial(paciente));
            }
            paciente.getHistorialMedico().getEntradas().add(entrada);
            pacienteRepository.save(paciente);
            return paciente.getHistorialMedico().getEntradas();
        }
        return List.of();
    }

    // Eliminar entrada del historial médico por índice
    @DeleteMapping("/{id}/historial/{indice}")
    public List<EntradaHistorial> deleteEntradaHistorial(@PathVariable String id, @PathVariable int indice) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null && paciente.getHistorialMedico() != null) {
            if (indice >= 0 && indice < paciente.getHistorialMedico().getEntradas().size()) {
                paciente.getHistorialMedico().getEntradas().remove(indice);
                pacienteRepository.save(paciente);
            }
            return paciente.getHistorialMedico().getEntradas();
        }
        return List.of();
    }

    // Obtener hábitos/síntomas diarios
    @GetMapping("/{id}/habitosysintomas")
    public List<HabitoSintoma> getHabitosYSintomas(@PathVariable String id) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null && paciente.getHabitosYSintomas() != null) {
            return paciente.getHabitosYSintomas();
        }
        return List.of();
    }

    // Agregar hábito/síntoma diario
    @PostMapping("/{id}/habitosysintomas")
    public List<HabitoSintoma> addHabitoSintoma(@PathVariable String id, @RequestBody HabitoSintoma habitoSintoma) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null) {
            if (paciente.getHabitosYSintomas() == null) {
                paciente.setHabitosYSintomas(new java.util.ArrayList<>());
            }
            paciente.getHabitosYSintomas().add(habitoSintoma);
            pacienteRepository.save(paciente);
            
            // Intentar generar alerta automática
            try {
                org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
                restTemplate.postForObject("http://localhost:8080/alertas/generar/" + id, habitoSintoma, Alerta.class);
            } catch (Exception e) {
                // Si falla la generación de alerta, no afectar la operación principal
                System.out.println("No se pudo generar alerta automática: " + e.getMessage());
            }
            
            return paciente.getHabitosYSintomas();
        }
        return List.of();
    }

    // Eliminar hábito/síntoma diario por índice
    @DeleteMapping("/{id}/habitosysintomas/{indice}")
    public List<HabitoSintoma> deleteHabitoSintoma(@PathVariable String id, @PathVariable int indice) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null && paciente.getHabitosYSintomas() != null) {
            if (indice >= 0 && indice < paciente.getHabitosYSintomas().size()) {
                paciente.getHabitosYSintomas().remove(indice);
                pacienteRepository.save(paciente);
            }
            return paciente.getHabitosYSintomas();
        }
        return List.of();
    }
} 