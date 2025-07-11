package com.grupo14.turnos.controller.restControllers;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.service.PrestadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestadores")
@Tag(name = "Prestadores", description = "API REST para la gestión de prestadores")
public class PrestadorRestController {

    private final PrestadorService prestadorService;

    public PrestadorRestController(PrestadorService prestadorService) {
        this.prestadorService = prestadorService;
    }

    @Operation(summary = "Listar todos los prestadores")
    @GetMapping
    public List<PrestadorDTO> listarTodos() {
        return prestadorService.listarTodos();
    }

    @Operation(summary = "Obtener un prestador por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<PrestadorDTO> obtenerPorId(@PathVariable Integer id) {
        PrestadorDTO dto = prestadorService.obtenerPorId(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear un nuevo prestador")
    @PostMapping
    public ResponseEntity<PrestadorDTO> crear(@RequestBody PrestadorDTO nuevo) {
        PrestadorDTO creado = prestadorService.crear(nuevo);
        return ResponseEntity.ok(creado);
    }

    @Operation(summary = "Actualizar un prestador existente")
    @PutMapping("/{id}")
    public ResponseEntity<PrestadorDTO> actualizar(@PathVariable Integer id, @RequestBody PrestadorDTO datos) {
        PrestadorDTO actualizado = prestadorService.actualizar(id, datos);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un prestador por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        prestadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
