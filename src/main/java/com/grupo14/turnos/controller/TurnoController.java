package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.service.TurnoService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listarTodos() {
        return ResponseEntity.ok(turnoService.listarTodos());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TurnoDTO>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(turnoService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<TurnoDTO> crear(@RequestBody TurnoDTO nuevo) {
        TurnoDTO creado = turnoService.crear(nuevo);
        return ResponseEntity.ok(creado);
    }
    
    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("turnos", turnoService.listarTodos());
        return "turnos";
    }

}