package com.vidasana.repository;

import com.vidasana.Alerta;
import com.vidasana.Paciente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends MongoRepository<Alerta, String> {
    List<Alerta> findByPaciente(Paciente paciente);
    List<Alerta> findByEstado(Alerta.EstadoAlerta estado);
    List<Alerta> findByPacienteAndEstado(Paciente paciente, Alerta.EstadoAlerta estado);
    long countByEstado(Alerta.EstadoAlerta estado);
} 