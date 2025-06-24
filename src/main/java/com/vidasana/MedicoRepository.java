package com.vidasana;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MedicoRepository extends MongoRepository<Medico, String> {
    Optional<Medico> findByDni(Integer dni);
} 