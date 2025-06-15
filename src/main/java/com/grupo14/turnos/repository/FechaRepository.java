package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Fecha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FechaRepository extends JpaRepository<Fecha, Long> {
}