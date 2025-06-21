package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PacienteRepository extends MongoRepository<Paciente, String> {
} 