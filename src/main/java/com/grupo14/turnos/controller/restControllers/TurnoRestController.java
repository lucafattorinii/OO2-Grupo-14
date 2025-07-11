package com.grupo14.turnos.controller.restControllers;

import com.grupo14.turnos.dto.TurnoConFechaDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.dto.TurnoFiltroDTO;
import com.grupo14.turnos.dto.TurnoVistaDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@Tag(name = "Turnos", description = "API REST para la gestión de turnos")
public class TurnoRestController {

    private final TurnoService turnoService;

    public TurnoRestController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @Operation(summary = "Obtiene un turno por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> obtenerPorId(@PathVariable long id) {
        TurnoDTO turno = turnoService.obtenerPorId(id);
        if (turno != null) {
            return ResponseEntity.ok(turno);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtiene todos los turnos")
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listarTodos() {
        return ResponseEntity.ok(turnoService.listarTodos());
    }

    @Operation(summary = "Filtra turnos según criterios")
    @PostMapping("/filtrar")
    public ResponseEntity<List<TurnoDTO>> filtrarTurnos(@RequestBody TurnoFiltroDTO filtro) {
        return ResponseEntity.ok(turnoService.filtrarTurnos(filtro));
    }

    @Operation(summary = "Obtiene los horarios ocupados")
    @GetMapping("/ocupados")
    public ResponseEntity<List<String>> horariosOcupados(
            @RequestParam String fecha,
            @RequestParam Long disponibilidadId,
            @RequestParam Long servicioId
    ) {
        return ResponseEntity.ok(turnoService.horariosOcupados(fecha, disponibilidadId, servicioId));
    }

    @Operation(summary = "Lista turnos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TurnoDTO>> listarPorEstado(@PathVariable String estado) {
        try {
            // El servicio ya valida el estado
            return ResponseEntity.ok(turnoService.listarPorEstado(estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Crea un nuevo turno")
    @PostMapping
    public ResponseEntity<TurnoConFechaDTO> crear(@RequestBody TurnoConFechaDTO dto) {
        try {
            TurnoConFechaDTO creado = turnoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualiza un turno existente")
    @PutMapping("/{id}")
    public ResponseEntity<TurnoConFechaDTO> actualizar(
            @PathVariable long id, 
            @RequestBody TurnoConFechaDTO dto) {
        try {
            TurnoConFechaDTO actualizado = turnoService.actualizar(id, dto);
            if (actualizado != null) {
                return ResponseEntity.ok(actualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Elimina un turno por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        try {
            turnoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualiza un turno completo con todos sus campos")
    @PutMapping("/{id}/completo")
    public ResponseEntity<Void> actualizarTurnoCompleto(
            @PathVariable long id,
            @RequestParam Long fechaId,
            @RequestParam String hora,
            @RequestParam String estado,
            @RequestParam long clienteId,
            @RequestParam long disponibilidadId,
            @RequestParam long servicioId) {
        try {
            turnoService.actualizarTurno(id, fechaId, LocalTime.parse(hora), estado, clienteId, disponibilidadId, servicioId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Cambia el estado de un turno")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable long id, 
            @RequestParam String nuevoEstado) {
        try {
            // Convertimos el estado a enum para validarlo
            try {
                EstadoTurno.valueOf(nuevoEstado.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            
            // Usamos el servicio para cambiar el estado
            turnoService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Lista todos los estados posibles de un turno")
    @GetMapping("/estados")
    public ResponseEntity<List<EstadoTurno>> listarEstados() {
        return ResponseEntity.ok(turnoService.listarEstados());
    }

    @Operation(summary = "Lista los turnos de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TurnoVistaDTO>> listarPorCliente(@PathVariable long clienteId) {
        return ResponseEntity.ok(turnoService.listarPorClienteId(clienteId));
    }
}
