package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/simulacion")
public class SimulacionController {
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private HistorialRepository historialRepository;

    // Simular hábitos y síntomas diarios para un paciente
    @PostMapping("/habitos/{pacienteId}")
    public ResponseEntity<Map<String, Object>> simularHabitos(
            @PathVariable String pacienteId,
            @RequestParam(defaultValue = "30") int dias) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> habitosGenerados = new ArrayList<>();
        LocalDate fechaInicio = LocalDate.now().minusDays(dias);
        
        for (int i = 0; i < dias; i++) {
            LocalDate fecha = fechaInicio.plusDays(i);
            
            // Generar 1-3 hábitos por día
            int numHabitos = ThreadLocalRandom.current().nextInt(1, 4);
            
            for (int j = 0; j < numHabitos; j++) {
                String habito = generarHabitoAleatorio();
                habitosGenerados.add(fecha + ": " + habito);
                
                // Agregar a la base de datos
                HabitoSintoma habitoSintoma = new HabitoSintoma();
                habitoSintoma.setFecha(fecha);
                habitoSintoma.setDescripcion(habito);
                
                paciente.getHabitosYSintomas().add(habitoSintoma);
            }
        }
        
        pacienteRepository.save(paciente);
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensaje", "Se generaron " + habitosGenerados.size() + " hábitos/síntomas");
        resultado.put("habitos", habitosGenerados);
        resultado.put("paciente", paciente.getNombre() + " " + paciente.getApellido());
        
        return ResponseEntity.ok(resultado);
    }

    // Simular datos de sensores
    @PostMapping("/sensores/{pacienteId}")
    public ResponseEntity<Map<String, Object>> simularSensores(
            @PathVariable String pacienteId,
            @RequestParam(defaultValue = "7") int dias) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> datosSensores = new ArrayList<>();
        LocalDate fechaInicio = LocalDate.now().minusDays(dias);
        
        for (int i = 0; i < dias; i++) {
            LocalDate fecha = fechaInicio.plusDays(i);
            
            Map<String, Object> datosDia = new HashMap<>();
            datosDia.put("fecha", fecha.toString());
            datosDia.put("sueño_horas", ThreadLocalRandom.current().nextInt(4, 10));
            datosDia.put("pasos", ThreadLocalRandom.current().nextInt(2000, 12000));
            datosDia.put("presion_sistolica", ThreadLocalRandom.current().nextInt(110, 140));
            datosDia.put("presion_diastolica", ThreadLocalRandom.current().nextInt(70, 90));
            datosDia.put("frecuencia_cardiaca", ThreadLocalRandom.current().nextInt(60, 100));
            datosDia.put("temperatura", 36.5 + ThreadLocalRandom.current().nextDouble(-1.0, 1.0));
            datosDia.put("peso", 70.0 + ThreadLocalRandom.current().nextDouble(-2.0, 2.0));
            
            datosSensores.add(datosDia);
        }
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensaje", "Se generaron datos de sensores para " + dias + " días");
        resultado.put("datos", datosSensores);
        resultado.put("paciente", paciente.getNombre() + " " + paciente.getApellido());
        
        return ResponseEntity.ok(resultado);
    }

    // Simular historial médico
    @PostMapping("/historial/{pacienteId}")
    public ResponseEntity<Map<String, Object>> simularHistorial(
            @PathVariable String pacienteId) {
        
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        Historial historial = historialRepository.findByPaciente(paciente);
        if (historial == null) {
            historial = new Historial(paciente);
        }

        List<String> entradasGeneradas = new ArrayList<>();
        
        // Generar diagnósticos
        for (int i = 0; i < 3; i++) {
            Medico medico = new Medico();
            medico.setNombre("Dr. " + generarNombreAleatorio());
            
            String diagnostico = generarDiagnosticoAleatorio();
            historial.agregarDiagnostico(medico, "Diagnóstico " + (i+1), diagnostico);
            entradasGeneradas.add("Diagnóstico: " + diagnostico);
        }
        
        // Generar recetas
        for (int i = 0; i < 2; i++) {
            Medico medico = new Medico();
            medico.setNombre("Dr. " + generarNombreAleatorio());
            
            String medicamento = generarMedicamentoAleatorio();
            historial.agregarReceta(medico, "Receta " + (i+1), "Receta médica", 
                                  medicamento, "1 comprimido cada 8 horas", "7 días");
            entradasGeneradas.add("Receta: " + medicamento);
        }
        
        // Generar exámenes
        for (int i = 0; i < 2; i++) {
            Medico medico = new Medico();
            medico.setNombre("Dr. " + generarNombreAleatorio());
            
            String examen = generarExamenAleatorio();
            historial.agregarExamen(medico, "Examen " + (i+1), "Resultado de examen", 
                                  "Resultado normal - " + examen);
            entradasGeneradas.add("Examen: " + examen);
        }
        
        // Generar alergias
        Medico medico = new Medico();
        medico.setNombre("Dr. " + generarNombreAleatorio());
        String alergia = generarAlergiaAleatoria();
        historial.agregarAlergia(medico, "Alergia conocida", "Alergia documentada", alergia);
        entradasGeneradas.add("Alergia: " + alergia);
        
        // Generar vacunas
        medico = new Medico();
        medico.setNombre("Dr. " + generarNombreAleatorio());
        String vacuna = generarVacunaAleatoria();
        historial.agregarVacuna(medico, "Vacuna aplicada", "Registro de vacunación", 
                              vacuna, "Lote-" + ThreadLocalRandom.current().nextInt(1000, 9999));
        entradasGeneradas.add("Vacuna: " + vacuna);
        
        historialRepository.save(historial);
        
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensaje", "Se generaron " + entradasGeneradas.size() + " entradas en el historial");
        resultado.put("entradas", entradasGeneradas);
        resultado.put("paciente", paciente.getNombre() + " " + paciente.getApellido());
        
        return ResponseEntity.ok(resultado);
    }

    // Simulación completa para un paciente
    @PostMapping("/completa/{pacienteId}")
    public ResponseEntity<Map<String, Object>> simulacionCompleta(
            @PathVariable String pacienteId,
            @RequestParam(defaultValue = "30") int diasHabitos,
            @RequestParam(defaultValue = "7") int diasSensores) {
        
        Map<String, Object> resultado = new HashMap<>();
        List<String> actividades = new ArrayList<>();
        
        // Simular hábitos
        try {
            ResponseEntity<Map<String, Object>> habitosResponse = simularHabitos(pacienteId, diasHabitos);
            if (habitosResponse.getStatusCode().is2xxSuccessful()) {
                actividades.add("✓ Hábitos y síntomas generados");
            }
        } catch (Exception e) {
            actividades.add("✗ Error en hábitos: " + e.getMessage());
        }
        
        // Simular sensores
        try {
            ResponseEntity<Map<String, Object>> sensoresResponse = simularSensores(pacienteId, diasSensores);
            if (sensoresResponse.getStatusCode().is2xxSuccessful()) {
                actividades.add("✓ Datos de sensores generados");
            }
        } catch (Exception e) {
            actividades.add("✗ Error en sensores: " + e.getMessage());
        }
        
        // Simular historial
        try {
            ResponseEntity<Map<String, Object>> historialResponse = simularHistorial(pacienteId);
            if (historialResponse.getStatusCode().is2xxSuccessful()) {
                actividades.add("✓ Historial médico generado");
            }
        } catch (Exception e) {
            actividades.add("✗ Error en historial: " + e.getMessage());
        }
        
        resultado.put("mensaje", "Simulación completa finalizada");
        resultado.put("actividades", actividades);
        resultado.put("pacienteId", pacienteId);
        
        return ResponseEntity.ok(resultado);
    }

    // Métodos auxiliares para generar datos aleatorios
    private String generarHabitoAleatorio() {
        String[] habitos = {
            "Dormí menos de 6 horas",
            "Dormí más de 8 horas",
            "Comí comida chatarra",
            "Hice ejercicio por 30 minutos",
            "No hice ejercicio",
            "Tomé 8 vasos de agua",
            "Tomé menos de 4 vasos de agua",
            "Fumé cigarrillos",
            "Tomé alcohol",
            "Dolor de cabeza",
            "Dolor de estómago",
            "Fiebre",
            "Tos seca",
            "Fatiga",
            "Ansiedad",
            "Estrés alto",
            "Meditación 15 minutos",
            "Leí un libro",
            "Miré TV por más de 3 horas",
            "Usé el celular por más de 4 horas"
        };
        return habitos[ThreadLocalRandom.current().nextInt(habitos.length)];
    }

    private String generarDiagnosticoAleatorio() {
        String[] diagnosticos = {
            "Hipertensión arterial leve",
            "Diabetes tipo 2 controlada",
            "Asma bronquial",
            "Artritis reumatoidea",
            "Depresión leve",
            "Ansiedad generalizada",
            "Insomnio crónico",
            "Gastritis crónica",
            "Migraña",
            "Obesidad grado 1"
        };
        return diagnosticos[ThreadLocalRandom.current().nextInt(diagnosticos.length)];
    }

    private String generarMedicamentoAleatorio() {
        String[] medicamentos = {
            "Paracetamol 500mg",
            "Ibuprofeno 400mg",
            "Omeprazol 20mg",
            "Loratadina 10mg",
            "Metformina 500mg",
            "Losartán 50mg",
            "Atorvastatina 20mg",
            "Amlodipino 5mg",
            "Sertralina 50mg",
            "Alprazolam 0.5mg"
        };
        return medicamentos[ThreadLocalRandom.current().nextInt(medicamentos.length)];
    }

    private String generarExamenAleatorio() {
        String[] examenes = {
            "Análisis de sangre completo",
            "Radiografía de tórax",
            "Electrocardiograma",
            "Ecografía abdominal",
            "Tomografía computada",
            "Resonancia magnética",
            "Endoscopia digestiva",
            "Colonoscopía",
            "Prueba de esfuerzo",
            "Holter de 24 horas"
        };
        return examenes[ThreadLocalRandom.current().nextInt(examenes.length)];
    }

    private String generarAlergiaAleatoria() {
        String[] alergias = {
            "Penicilina",
            "Sulfamidas",
            "Polen",
            "Ácaros del polvo",
            "Cacahuetes",
            "Mariscos",
            "Látex",
            "Picadura de abeja",
            "Aspirina",
            "Ibuprofeno"
        };
        return alergias[ThreadLocalRandom.current().nextInt(alergias.length)];
    }

    private String generarVacunaAleatoria() {
        String[] vacunas = {
            "COVID-19 (Pfizer)",
            "COVID-19 (Moderna)",
            "Influenza estacional",
            "Neumococo",
            "Hepatitis B",
            "Tétanos",
            "Difteria",
            "Sarampión",
            "Varicela",
            "VPH"
        };
        return vacunas[ThreadLocalRandom.current().nextInt(vacunas.length)];
    }

    private String generarNombreAleatorio() {
        String[] nombres = {
            "García", "Rodríguez", "López", "Martínez", "González",
            "Pérez", "Sánchez", "Ramírez", "Torres", "Flores",
            "Rivera", "Morales", "Cruz", "Ortiz", "Reyes",
            "Moreno", "Jiménez", "Díaz", "Romero", "Herrera"
        };
        return nombres[ThreadLocalRandom.current().nextInt(nombres.length)];
    }
} 