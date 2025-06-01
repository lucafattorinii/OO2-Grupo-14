package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.service.DireccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> listarTodos() {
        return ResponseEntity.ok(direccionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(direccionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> crear(@RequestBody DireccionDTO nuevo) {
        return ResponseEntity.ok(direccionService.crear(nuevo));
    }
    
    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("direcciones", direccionService.listarTodos());
        return "direcciones";
    }

}
