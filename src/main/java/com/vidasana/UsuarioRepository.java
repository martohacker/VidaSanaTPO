package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByRol(String rol);
    Optional<Usuario> findByPacienteId(String pacienteId);
    Optional<Usuario> findByMedicoId(String medicoId);
} 