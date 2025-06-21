package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicoRepository extends MongoRepository<Medico, String> {
} 