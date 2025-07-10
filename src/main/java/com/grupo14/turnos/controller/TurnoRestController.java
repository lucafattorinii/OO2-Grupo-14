package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.TurnoConFechaDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.dto.TurnoFiltroDTO;
import com.grupo14.turnos.dto.TurnoVistaDTO;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.service.TurnoService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
public class TurnoRestController {

    private final TurnoService turnoService;

    public TurnoRestController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping("/{id}")
    public TurnoDTO obtenerPorId(@PathVariable long id) {
        return turnoService.obtenerPorId(id);
    }

    @GetMapping
    public List<TurnoDTO> listarTodos() {
        return turnoService.listarTodos();
    }

    @PostMapping("/filtrar")
    public List<TurnoDTO> filtrarTurnos(@RequestBody TurnoFiltroDTO filtro) {
        return turnoService.filtrarTurnos(filtro);
    }

    @GetMapping("/ocupados")
    public List<String> horariosOcupados(
            @RequestParam String fecha,
            @RequestParam Long disponibilidadId,
            @RequestParam Long servicioId
    ) {
        return turnoService.horariosOcupados(fecha, disponibilidadId, servicioId);
    }

    @GetMapping("/estado/{estado}")
    public List<TurnoDTO> listarPorEstado(@PathVariable String estado) {
        return turnoService.listarPorEstado(estado);
    }

    @PostMapping
    public TurnoConFechaDTO crear(@RequestBody TurnoConFechaDTO dto) {
        return turnoService.crear(dto);
    }

    @PutMapping("/{id}")
    public TurnoConFechaDTO actualizar(@PathVariable long id, @RequestBody TurnoConFechaDTO dto) {
        return turnoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        turnoService.eliminar(id);
    }

    @PutMapping("/{id}/completo")
    public void actualizarTurnoCompleto(
        @PathVariable long id,
        @RequestParam Long fechaId,
        @RequestParam String hora,
        @RequestParam String estado,
        @RequestParam long clienteId,
        @RequestParam long disponibilidadId,
        @RequestParam long servicioId
    ) {
        turnoService.actualizarTurno(id, fechaId, LocalTime.parse(hora), estado, clienteId, disponibilidadId, servicioId);
    }

    @PatchMapping("/{id}/estado")
    public void cambiarEstado(@PathVariable long id, @RequestParam String nuevoEstado) {
        turnoService.cambiarEstado(id, nuevoEstado);
    }

    @GetMapping("/estados")
    public List<EstadoTurno> listarEstados() {
        return turnoService.listarEstados();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<TurnoVistaDTO> listarPorCliente(@PathVariable long clienteId) {
        return turnoService.listarPorClienteId(clienteId);
    }
}
