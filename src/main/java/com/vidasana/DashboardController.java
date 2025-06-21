package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

@RestController
public class DashboardController {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private TurnoRepository turnoRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private HabitoYSintomaRepository habitoYSintomaRepository;

    @GetMapping("/dashboard")
    public DashboardDTO getDashboard() {
        long totalPacientes = pacienteRepository.count();
        long totalTurnosPendientes = turnoRepository.countByEstado("pendiente");

        Map<String, Integer> pacientesPorMedico = new HashMap<>();
        medicoRepository.findAll().forEach(medico -> {
            String nombre = medico.getNombre() + " " + medico.getApellido();
            int cantidad = medico.getPacientes() != null ? medico.getPacientes().size() : 0;
            pacientesPorMedico.put(nombre, cantidad);
        });

        List<HabitoYSintoma> habitos = habitoYSintomaRepository.findAll();
        double promedioSueno = 0;
        if (!habitos.isEmpty()) {
            promedioSueno = habitos.stream().mapToInt(HabitoYSintoma::getSueno).average().orElse(0);
        }

        return new DashboardDTO(totalPacientes, totalTurnosPendientes, pacientesPorMedico, promedioSueno);
    }
} 