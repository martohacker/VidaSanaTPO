package com.vidasana.controller;

import com.vidasana.Alerta;
import com.vidasana.Paciente;
import com.vidasana.PacienteRepository;
import com.vidasana.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {
    @Autowired
    private AlertaService alertaService;
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Alerta> listarAlertas() {
        return alertaService.obtenerAlertasPendientes();
    }

    @GetMapping("/paciente/{id}")
    public List<Alerta> listarPorPaciente(@PathVariable String id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.map(alertaService::obtenerAlertasPorPaciente).orElse(List.of());
    }

    @PostMapping
    public Alerta crearAlerta(@RequestBody Alerta alerta) {
        return alertaService.crearAlerta(alerta);
    }

    @PutMapping("/{id}/resolver")
    public Alerta resolverAlerta(@PathVariable String id) {
        return alertaService.resolverAlerta(id).orElseThrow();
    }
} 