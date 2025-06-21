package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class RiesgoController {
    @Autowired
    private HabitoYSintomaRepository habitoRepo;

    @GetMapping("/riesgo/{pacienteId}")
    public EvaluacionRiesgoDTO evaluarRiesgo(@PathVariable String pacienteId) {
        List<HabitoYSintoma> habitos = habitoRepo.findByPacienteId(pacienteId);
        if (habitos.isEmpty()) {
            return new EvaluacionRiesgoDTO(pacienteId, "DESCONOCIDO", "No hay datos recientes.");
        }
        HabitoYSintoma reciente = habitos.get(habitos.size() - 1);
        boolean pocoSueno = reciente.getSueno() < 6;
        long fiebreCount = reciente.getSintomas() != null ? reciente.getSintomas().stream().filter(s -> s.toLowerCase().contains("fiebre")).count() : 0;

        if (pocoSueno && fiebreCount > 0) {
            return new EvaluacionRiesgoDTO(pacienteId, "ALTO", "Consultar médico urgentemente.");
        } else if (pocoSueno || fiebreCount > 0) {
            return new EvaluacionRiesgoDTO(pacienteId, "MODERADO", "Vigilar síntomas y consultar si empeora.");
        } else {
            return new EvaluacionRiesgoDTO(pacienteId, "BAJO", "Sin riesgo relevante.");
        }
    }
} 