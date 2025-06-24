package com.vidasana.service;

import com.vidasana.Alerta;
import com.vidasana.Paciente;
import com.vidasana.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {
    @Autowired
    private AlertaRepository alertaRepository;

    public Alerta crearAlerta(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    public List<Alerta> obtenerAlertasPorPaciente(Paciente paciente) {
        return alertaRepository.findByPaciente(paciente);
    }

    public List<Alerta> obtenerAlertasPendientes() {
        return alertaRepository.findByEstado(Alerta.EstadoAlerta.PENDIENTE);
    }

    public Optional<Alerta> resolverAlerta(String id) {
        Optional<Alerta> alertaOpt = alertaRepository.findById(id);
        alertaOpt.ifPresent(alerta -> {
            alerta.setEstado(Alerta.EstadoAlerta.RESUELTA);
            alertaRepository.save(alerta);
        });
        return alertaOpt;
    }

    public List<Alerta> obtenerAlertasPorPacienteYEstado(Paciente paciente, Alerta.EstadoAlerta estado) {
        return alertaRepository.findByPacienteAndEstado(paciente, estado);
    }
} 