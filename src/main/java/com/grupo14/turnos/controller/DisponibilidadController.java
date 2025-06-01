package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.service.DisponibilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping
    public ResponseEntity<List<DisponibilidadDTO>> listarTodos() {
        return ResponseEntity.ok(disponibilidadService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(disponibilidadService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<DisponibilidadDTO> crear(@RequestBody DisponibilidadDTO nuevo) {
        DisponibilidadDTO creado = disponibilidadService.crear(nuevo);
        return ResponseEntity.ok(creado);
    }
    
    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("disponibilidades", disponibilidadService.listarTodos());
        return "disponibilidades";
    }

}