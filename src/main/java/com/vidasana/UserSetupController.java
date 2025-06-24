package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/setup")
public class UserSetupController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;

    // Endpoint para verificar usuarios existentes
    @GetMapping("/check-users")
    public Map<String, Object> checkUsers() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            var usuarios = usuarioRepository.findAll();
            response.put("success", true);
            response.put("totalUsuarios", usuarios.size());
            response.put("usuarios", usuarios);
            
            // Verificar usuarios específicos
            var admin = usuarioRepository.findByUsername("admin");
            var medico = usuarioRepository.findByUsername("medico1");
            var paciente = usuarioRepository.findByUsername("paciente1");
            
            response.put("adminExists", admin.isPresent());
            response.put("medicoExists", medico.isPresent());
            response.put("pacienteExists", paciente.isPresent());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }

    // Endpoint para crear usuarios manualmente
    @PostMapping("/create-demo-users")
    public Map<String, Object> createDemoUsers() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar si ya existen
            if (usuarioRepository.findByUsername("admin").isPresent()) {
                response.put("message", "Los usuarios ya existen");
                return checkUsers();
            }
            
            // Crear admin
            Usuario admin = new Usuario("admin", "admin123", "ADMIN", "Administrador del Sistema");
            admin = usuarioRepository.save(admin);
            
            // Crear médico
            Medico medicoEntity = new Medico("Juan", "Pérez", "juan.perez@vidasana.com", 12345678, 45, java.time.LocalDate.of(1978, 3, 10), "Cardiología");
            medicoEntity = medicoRepository.save(medicoEntity);
            
            Usuario medico = new Usuario("medico1", "medico123", "MEDICO", "Dr. Juan Pérez");
            medico.setMedicoId(medicoEntity.getId());
            medico = usuarioRepository.save(medico);
            
            // Crear paciente
            Paciente pacienteEntity = new Paciente("María", "González", "maria.gonzalez@email.com", 87654321, "Argentina", 35, java.time.LocalDate.of(1988, 5, 15));
            pacienteEntity.setSueño("8 horas");
            pacienteEntity = pacienteRepository.save(pacienteEntity);
            
            Usuario paciente = new Usuario("paciente1", "paciente123", "PACIENTE", "María González");
            paciente.setPacienteId(pacienteEntity.getId());
            paciente = usuarioRepository.save(paciente);
            
            response.put("success", true);
            response.put("message", "Usuarios de demostración creados exitosamente");
            response.put("adminId", admin.getId());
            response.put("medicoId", medico.getId());
            response.put("pacienteId", paciente.getId());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
} 