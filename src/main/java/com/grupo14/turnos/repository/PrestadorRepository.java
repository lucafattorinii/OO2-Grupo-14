package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Prestador;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    Optional<Prestador> findByEmail(String email);
    Optional<Prestador> findTopByOrderByIdAsc();
}
