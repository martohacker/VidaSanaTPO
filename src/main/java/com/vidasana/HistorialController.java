package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/historial")
public class HistorialController {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private HistorialRepository historialRepository;

    // Obtener historial completo de un paciente
    @GetMapping("/{pacienteId}")
    public ResponseEntity<Historial> getHistorial(@PathVariable String pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = historialRepository.findByPaciente(paciente);
        if (historial == null) {
            historial = new Historial(paciente);
            historialRepository.save(historial);
        }
        
        return ResponseEntity.ok(historial);
    }

    // Obtener entradas por tipo
    @GetMapping("/{pacienteId}/tipo/{tipo}")
    public ResponseEntity<List<EntradaHistorial>> getEntradasPorTipo(
            @PathVariable String pacienteId, 
            @PathVariable String tipo) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = historialRepository.findByPaciente(paciente);
        if (historial == null) {
            return ResponseEntity.ok(List.of());
        }
        
        try {
            EntradaHistorial.TipoEntrada tipoEntrada = EntradaHistorial.TipoEntrada.valueOf(tipo.toUpperCase());
            List<EntradaHistorial> entradas = historial.getEntradasPorTipo(tipoEntrada);
            return ResponseEntity.ok(entradas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Agregar nota general
    @PostMapping("/{pacienteId}/nota")
    public ResponseEntity<EntradaHistorial> agregarNota(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        return agregarEntrada(pacienteId, EntradaHistorial.TipoEntrada.NOTA, request);
    }

    // Agregar diagnóstico
    @PostMapping("/{pacienteId}/diagnostico")
    public ResponseEntity<EntradaHistorial> agregarDiagnostico(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        return agregarEntrada(pacienteId, EntradaHistorial.TipoEntrada.DIAGNOSTICO, request);
    }

    // Agregar receta
    @PostMapping("/{pacienteId}/receta")
    public ResponseEntity<EntradaHistorial> agregarReceta(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = obtenerOCrearHistorial(paciente);
        
        Medico medico = new Medico();
        medico.setNombre(request.get("medico"));
        
        historial.agregarReceta(
            medico,
            request.get("titulo"),
            request.get("descripcion"),
            request.get("medicamentos"),
            request.get("dosis"),
            request.get("duracion")
        );
        
        historialRepository.save(historial);
        return ResponseEntity.ok(historial.getEntradas().get(historial.getEntradas().size() - 1));
    }

    // Agregar examen
    @PostMapping("/{pacienteId}/examen")
    public ResponseEntity<EntradaHistorial> agregarExamen(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = obtenerOCrearHistorial(paciente);
        
        Medico medico = new Medico();
        medico.setNombre(request.get("medico"));
        
        historial.agregarExamen(
            medico,
            request.get("titulo"),
            request.get("descripcion"),
            request.get("resultado")
        );
        
        historialRepository.save(historial);
        return ResponseEntity.ok(historial.getEntradas().get(historial.getEntradas().size() - 1));
    }

    // Agregar tratamiento
    @PostMapping("/{pacienteId}/tratamiento")
    public ResponseEntity<EntradaHistorial> agregarTratamiento(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = obtenerOCrearHistorial(paciente);
        
        Medico medico = new Medico();
        medico.setNombre(request.get("medico"));
        
        historial.agregarTratamiento(
            medico,
            request.get("titulo"),
            request.get("descripcion"),
            request.get("duracion")
        );
        
        historialRepository.save(historial);
        return ResponseEntity.ok(historial.getEntradas().get(historial.getEntradas().size() - 1));
    }

    // Agregar alergia
    @PostMapping("/{pacienteId}/alergia")
    public ResponseEntity<EntradaHistorial> agregarAlergia(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = obtenerOCrearHistorial(paciente);
        
        Medico medico = new Medico();
        medico.setNombre(request.get("medico"));
        
        historial.agregarAlergia(
            medico,
            request.get("titulo"),
            request.get("descripcion"),
            request.get("alergeno")
        );
        
        historialRepository.save(historial);
        return ResponseEntity.ok(historial.getEntradas().get(historial.getEntradas().size() - 1));
    }

    // Agregar vacuna
    @PostMapping("/{pacienteId}/vacuna")
    public ResponseEntity<EntradaHistorial> agregarVacuna(
            @PathVariable String pacienteId,
            @RequestBody Map<String, String> request) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = obtenerOCrearHistorial(paciente);
        
        Medico medico = new Medico();
        medico.setNombre(request.get("medico"));
        
        historial.agregarVacuna(
            medico,
            request.get("titulo"),
            request.get("descripcion"),
            request.get("vacuna"),
            request.get("lote")
        );
        
        historialRepository.save(historial);
        return ResponseEntity.ok(historial.getEntradas().get(historial.getEntradas().size() - 1));
    }

    // Método auxiliar para agregar entradas simples
    private ResponseEntity<EntradaHistorial> agregarEntrada(
            String pacienteId, 
            EntradaHistorial.TipoEntrada tipo, 
            Map<String, String> request) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        
        Historial historial = obtenerOCrearHistorial(paciente);
        
        Medico medico = new Medico();
        medico.setNombre(request.get("medico"));
        
        switch (tipo) {
            case NOTA:
                historial.agregarNota(medico, request.get("titulo"), request.get("descripcion"));
                break;
            case DIAGNOSTICO:
                historial.agregarDiagnostico(medico, request.get("titulo"), request.get("descripcion"));
                break;
        }
        
        historialRepository.save(historial);
        return ResponseEntity.ok(historial.getEntradas().get(historial.getEntradas().size() - 1));
    }

    // Método auxiliar para obtener o crear historial
    private Historial obtenerOCrearHistorial(Paciente paciente) {
        Historial historial = historialRepository.findByPaciente(paciente);
        if (historial == null) {
            historial = new Historial(paciente);
        }
        return historial;
    }
} 