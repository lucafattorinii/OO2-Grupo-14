package com.grupo14.turnos.repository;

import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Disponibilidad;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.modelo.Fecha;
import com.grupo14.turnos.modelo.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long>, JpaSpecificationExecutor<Turno> {
	
	List<Turno> findByEstado(String estado);
    List<Turno> findByEstado(EstadoTurno estado);
    List<Turno> findByClienteId(Long clienteId);
    
    boolean existsByFechaAndHoraAndDisponibilidadAndFecha_Direccion(
    	    Fecha fecha,
    	    LocalTime hora,
    	    Disponibilidad disponibilidad,
    	    Direccion direccion
    	);
    
    @Query("SELECT COUNT(t) FROM Turno t JOIN t.disponibilidad d JOIN d.servicios s WHERE s.id = :servicioId")
    long countTurnosByServicioId(@Param("servicioId") Long servicioId);
    
    @Query("SELECT COUNT(t) > 0 FROM Turno t WHERE t.servicio.id = :servicioId AND (t.estado = 'PENDIENTE' OR t.estado = 'CONFIRMADO')")
    boolean existsTurnoNoCanceladoByServicioId(@Param("servicioId") Long servicioId);
	
    @Modifying
    @Transactional
    @Query("DELETE FROM Turno t WHERE t.servicio.id = :servicioId AND t.estado = 'CANCELADO'")
    void deleteTurnosCanceladosByServicioId(@Param("servicioId") Long servicioId);
    
    @Query("SELECT t FROM Turno t JOIN t.cliente c WHERE " +
           "(:fechaDesde IS NULL OR t.fecha.fecha >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR t.fecha.fecha <= :fechaHasta) AND " +
           "(:clienteId IS NULL OR t.cliente.id = :clienteId) AND " +
           "(:servicioId IS NULL OR t.servicio.id = :servicioId) AND " +
           "(:estado IS NULL OR t.estado = :estado) AND " +
           "(:nombreCliente IS NULL OR LOWER(CONCAT(c.nombre, ' ', c.apellido)) LIKE LOWER(CONCAT('%', :nombreCliente, '%')))")
    List<Turno> filtrarTurnos(
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta,
        @Param("clienteId") Long clienteId,
        @Param("servicioId") Long servicioId,
        @Param("estado") EstadoTurno estado,
        @Param("nombreCliente") String nombreCliente
    );
}