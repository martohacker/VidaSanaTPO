package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;
    
    @Autowired
    private HistorialRepository historialRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar si no hay usuarios
        if (usuarioRepository.count() == 0) {
            initializeDemoData();
        }
    }
    
    private void initializeDemoData() {
        try {
            System.out.println("Iniciando creación de datos de demostración...");
            
            // Crear usuarios de demostración
            
            // Admin
            Usuario admin = new Usuario("admin", "admin123", "ADMIN", "Administrador del Sistema");
            admin = usuarioRepository.save(admin);
            System.out.println("Admin creado con ID: " + admin.getId());
            
            // Médico
            System.out.println("Creando médico...");
            Medico medicoEntity = new Medico("Juan", "Pérez", "juan.perez@vidasana.com", 12345678, 45, java.time.LocalDate.of(1978, 3, 10), "Cardiología");
            medicoEntity = medicoRepository.save(medicoEntity);
            System.out.println("Médico creado con ID: " + medicoEntity.getId());
            
            Usuario medico = new Usuario("medico1", "medico123", "MEDICO", "Dr. Juan Pérez");
            medico.setMedicoId(medicoEntity.getId());
            medico = usuarioRepository.save(medico);
            System.out.println("Usuario médico creado con ID: " + medico.getId());
            
            // Paciente
            System.out.println("Creando paciente...");
            Paciente pacienteEntity = new Paciente("María", "González", "maria.gonzalez@email.com", 87654321, "Argentina", 35, java.time.LocalDate.of(1988, 5, 15));
            pacienteEntity.setSueño("8 horas");
            pacienteEntity = pacienteRepository.save(pacienteEntity);
            System.out.println("Paciente creado con ID: " + pacienteEntity.getId());
            
            // Crear historial médico para el paciente
            Historial historial = new Historial(pacienteEntity);
            historial = historialRepository.save(historial);
            pacienteEntity.setHistorialMedico(historial);
            pacienteEntity = pacienteRepository.save(pacienteEntity);
            System.out.println("Historial médico creado con ID: " + historial.getId());
            
            Usuario paciente = new Usuario("paciente1", "paciente123", "PACIENTE", "María González");
            paciente.setPacienteId(pacienteEntity.getId());
            paciente = usuarioRepository.save(paciente);
            System.out.println("Usuario paciente creado con ID: " + paciente.getId());
            
            System.out.println("=== DATOS DE DEMOSTRACIÓN INICIALIZADOS ===");
            System.out.println("- Admin: admin / admin123");
            System.out.println("- Médico: medico1 / medico123");
            System.out.println("- Paciente: paciente1 / paciente123");
            System.out.println("===========================================");
            
        } catch (Exception e) {
            System.err.println("Error al inicializar datos de demostración: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 