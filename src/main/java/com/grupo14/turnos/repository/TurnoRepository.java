package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    List<Turno> findByEstado(String estado);
    List<Turno> findByEstado(EstadoTurno estado);
}