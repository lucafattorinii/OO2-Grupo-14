package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Integer> {
    Optional<Prestador> findByEmail(String email); 
}
