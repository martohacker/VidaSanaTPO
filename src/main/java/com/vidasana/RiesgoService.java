package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vidasana.service.AlertaService;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiesgoService {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private HistorialRepository historialRepository;
    @Autowired
    private AlertaService alertaService;

    public EvaluacionRiesgoDTO calcularRiesgo(String pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) return null;
        
        int score = 0;
        List<String> recomendaciones = new ArrayList<>();
        List<String> sintomasPreocupantes = new ArrayList<>();
        
        // Analizar la descripción de todos los hábitos/síntomas (sin filtrar por fecha)
        for (HabitoSintoma hs : paciente.getHabitosYSintomas()) {
            String desc = hs.getDescripcion() != null ? hs.getDescripcion().toLowerCase() : "";
            if (desc.contains("<6") || desc.contains("no dormir") || desc.contains("menos de 6 horas")) {
                score += 20;
                recomendaciones.add("Dormir más de 6 horas por día");
            }
            if (desc.contains("chatarra") || desc.contains("fritura") || desc.contains("azúcar")) {
                score += 15;
                recomendaciones.add("Mejorar la alimentación");
            }
            if (desc.contains("dolor") || desc.contains("fiebre") || desc.contains("mareo") || desc.contains("nausea") || desc.contains("sangrado") || desc.contains("inflamacion") || desc.contains("panza") || desc.contains("cabeza") || desc.contains("estómago")) {
                score += 25;
                recomendaciones.add("Consultar al médico por síntomas persistentes");
                sintomasPreocupantes.add(hs.getDescripcion());
            }
            if (desc.contains("fumar") || desc.contains("alcohol") || desc.contains("drogas")) {
                score += 20;
                recomendaciones.add("Evitar hábitos tóxicos");
            }
        }
        
        // Revisar historial médico flexible
        if (paciente.getHistorialMedico() != null) {
            Historial historial = historialRepository.findById(paciente.getHistorialMedico().getId()).orElse(null);
            if (historial != null) {
                for (EntradaHistorial entrada : historial.getEntradas()) {
                    String texto = (entrada.getDescripcion() != null ? entrada.getDescripcion() : "") + " " + (entrada.getTitulo() != null ? entrada.getTitulo() : "");
                    if (texto.toLowerCase().contains("crónico") || texto.toLowerCase().contains("crónica")) {
                        score += 30;
                        recomendaciones.add("Controlar enfermedades crónicas");
                    }
                }
            }
        }
        
        String nivel = "BAJO";
        if (score >= 60) nivel = "ALTO";
        else if (score >= 30) nivel = "MEDIO";
        
        // Generar alertas automáticas
        generarAlertasAutomaticas(paciente, score, nivel, sintomasPreocupantes);
        
        return new EvaluacionRiesgoDTO(score, nivel, recomendaciones);
    }
    
    private void generarAlertasAutomaticas(Paciente paciente, int score, String nivel, List<String> sintomasPreocupantes) {
        // Alerta por riesgo alto
        if (nivel.equals("ALTO")) {
            Alerta alertaRiesgo = new Alerta(paciente, Alerta.TipoAlerta.RIESGO, 
                "Riesgo alto detectado (Score: " + score + "). Se requiere atención médica inmediata.");
            alertaService.crearAlerta(alertaRiesgo);
        }
        
        // Alertas por síntomas preocupantes
        for (String sintoma : sintomasPreocupantes) {
            Alerta alertaSintoma = new Alerta(paciente, Alerta.TipoAlerta.SINTOMA, 
                "Síntoma preocupante reportado: " + sintoma);
            alertaService.crearAlerta(alertaSintoma);
        }
        
        // Alerta por riesgo medio
        if (nivel.equals("MEDIO")) {
            Alerta alertaRiesgo = new Alerta(paciente, Alerta.TipoAlerta.RIESGO, 
                "Riesgo medio detectado (Score: " + score + "). Se recomienda monitoreo.");
            alertaService.crearAlerta(alertaRiesgo);
        }
    }
} 