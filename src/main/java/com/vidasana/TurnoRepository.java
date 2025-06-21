package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TurnoRepository extends MongoRepository<Turno, String> {
    long countByEstado(String estado);
} 