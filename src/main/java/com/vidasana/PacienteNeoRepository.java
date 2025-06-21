package com.vidasana;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteNeoRepository extends Neo4jRepository<PacienteNeo4j, String> {} 