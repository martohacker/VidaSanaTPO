package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PacienteRepository extends MongoRepository<Paciente, String> {
    Optional<Paciente> findByDni(int dni);
} 