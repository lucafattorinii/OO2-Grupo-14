package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
}