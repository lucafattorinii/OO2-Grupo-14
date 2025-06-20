package com.grupo14.turnos.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grupo14.turnos.modelo.Fecha;

public interface FechaRepository extends JpaRepository<Fecha, Long> {

    @Query("SELECT f FROM Fecha f WHERE f.fecha = :fecha AND f.direccion.idDireccion = :direccionId")
    Optional<Fecha> findByFechaAndDireccionId(@Param("fecha") LocalDate fecha, @Param("direccionId") Long direccionId);

}