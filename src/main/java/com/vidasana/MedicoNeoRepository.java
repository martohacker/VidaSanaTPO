package com.vidasana;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoNeoRepository extends Neo4jRepository<MedicoNeo4j, String> {} 