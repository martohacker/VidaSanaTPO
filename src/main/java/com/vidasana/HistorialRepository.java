package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends MongoRepository<Historial, String> {
    Historial findByPaciente(Paciente paciente);
} 