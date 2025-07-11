package com.grupo14.turnos.controller.restControllers;

import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.service.ServicioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "Servicios", description = "Operaciones CRUD sobre los servicios ofrecidos")
public class ServicioRestController {

    private final ServicioService servicioService;

    public ServicioRestController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    // API REST: Listar todos
    @Operation(summary = "Listar todos los servicios")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServicioDTO> listarTodosJson() {
        return servicioService.listarTodos();
    }

    // API REST: Obtener uno por ID
    @Operation(summary = "Obtener un servicio por su ID")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServicioDTO obtenerJson(@PathVariable Long id) {
        return servicioService.obtenerPorId(id);
    }

    // API REST: Crear con JSON
    @Operation(summary = "Crear un nuevo servicio")
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ServicioDTO crearJson(@RequestBody ServicioDTO nuevo) {
        return servicioService.crear(nuevo);
    }

    // API REST: Actualizar
    @Operation(summary = "Actualizar un servicio existente")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                           produces = MediaType.APPLICATION_JSON_VALUE)
    public ServicioDTO actualizar(@PathVariable Long id, @RequestBody ServicioDTO dto) {
        return servicioService.actualizar(id, dto);
    }

    // API REST: Eliminar
    @Operation(summary = "Eliminar un servicio por su ID")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
    }
}
