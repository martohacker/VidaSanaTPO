package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HabitoYSintomaRepository extends MongoRepository<HabitoYSintoma, String> {
    List<HabitoYSintoma> findByPacienteId(String pacienteId);
} 