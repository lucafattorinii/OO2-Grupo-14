package com.grupo14.turnos.controller.restControllers;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.service.DisponibilidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
@Tag(name = "Disponibilidades", description = "API REST para la gestión de disponibilidades de horarios")
public class DisponibilidadRestController {

    private final DisponibilidadService disponibilidadService;

    @Autowired
    public DisponibilidadRestController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @Operation(summary = "Obtiene una disponibilidad por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadDTO> obtenerPorId(@PathVariable Long id) {
        try {
            DisponibilidadDTO disponibilidad = disponibilidadService.obtenerPorId(id);
            return ResponseEntity.ok(disponibilidad);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtiene todas las disponibilidades")
    @ApiResponse(responseCode = "200", description = "Lista de disponibilidades obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<DisponibilidadDTO>> listarTodas() {
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos();
        return ResponseEntity.ok(disponibilidades);
    }

    @Operation(summary = "Crea una nueva disponibilidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Disponibilidad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la disponibilidad inválidos")
    })
    @PostMapping
    public ResponseEntity<DisponibilidadDTO> crear(@RequestBody DisponibilidadDTO disponibilidadDTO) {
        try {
            DisponibilidadDTO nuevaDisponibilidad = disponibilidadService.crear(disponibilidadDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDisponibilidad);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualiza una disponibilidad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la disponibilidad inválidos"),
        @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadDTO> actualizar(
            @PathVariable Long id, 
            @RequestBody DisponibilidadDTO disponibilidadDTO) {
        try {
            DisponibilidadDTO actualizada = disponibilidadService.actualizar(id, disponibilidadDTO);
            return ResponseEntity.ok(actualizada);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Elimina una disponibilidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disponibilidad eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            disponibilidadService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtiene las disponibilidades por día de la semana")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de disponibilidades filtradas por día"),
        @ApiResponse(responseCode = "400", description = "Nombre de día no válido")
    })
    @GetMapping("/dia/{diaSemana}")
    public ResponseEntity<List<DisponibilidadDTO>> obtenerPorDiaSemana(
            @Parameter(description = "Día de la semana en mayúsculas (ej: LUNES, MARTES, etc.)")
            @PathVariable String diaSemana) {
        try {
            List<DisponibilidadDTO> disponibilidades = disponibilidadService
                .obtenerPorDiaSemana(diaSemana);
            return ResponseEntity.ok(disponibilidades);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
