package com.vidasana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PacienteRepository pacienteRepository;
    
    @Autowired
    private MedicoRepository medicoRepository;

    // Login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        if (username == null || password == null) {
            response.put("success", false);
            response.put("message", "Username y password son requeridos");
            return response;
        }
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                response.put("success", true);
                response.put("usuario", usuario);
                response.put("message", "Login exitoso");
            } else {
                response.put("success", false);
                response.put("message", "Password incorrecto");
            }
        } else {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
        }
        
        return response;
    }

    // Obtener información del usuario
    @GetMapping("/usuario/{id}")
    public Map<String, Object> getUsuarioInfo(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            response.put("success", true);
            response.put("usuario", usuario);
            
            // Agregar información adicional según el rol
            if ("PACIENTE".equals(usuario.getRol()) && usuario.getPacienteId() != null) {
                pacienteRepository.findById(usuario.getPacienteId()).ifPresent(paciente -> {
                    response.put("paciente", paciente);
                });
            } else if ("MEDICO".equals(usuario.getRol()) && usuario.getMedicoId() != null) {
                medicoRepository.findById(usuario.getMedicoId()).ifPresent(medico -> {
                    response.put("medico", medico);
                });
            }
        } else {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
        }
        
        return response;
    }

    // Crear usuario (solo para desarrollo/demo)
    @PostMapping("/registrar")
    public Map<String, Object> registrar(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        
        // Verificar si el username ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            response.put("success", false);
            response.put("message", "El username ya existe");
            return response;
        }
        
        try {
            Usuario nuevoUsuario = usuarioRepository.save(usuario);
            response.put("success", true);
            response.put("usuario", nuevoUsuario);
            response.put("message", "Usuario registrado exitosamente");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al registrar usuario: " + e.getMessage());
        }
        
        return response;
    }

    // Listar usuarios por rol
    @GetMapping("/usuarios/{rol}")
    public Map<String, Object> getUsuariosByRol(@PathVariable String rol) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            var usuarios = usuarioRepository.findByRol(rol.toUpperCase());
            response.put("success", true);
            response.put("usuarios", usuarios);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener usuarios: " + e.getMessage());
        }
        
        return response;
    }

    // Listar todos los usuarios (para debug)
    @GetMapping("/usuarios")
    public Map<String, Object> getAllUsuarios() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            var usuarios = usuarioRepository.findAll();
            response.put("success", true);
            response.put("usuarios", usuarios);
            response.put("total", usuarios.size());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener usuarios: " + e.getMessage());
        }
        
        return response;
    }
} 