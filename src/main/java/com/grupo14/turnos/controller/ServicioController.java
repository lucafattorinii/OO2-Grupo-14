package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ServicioDTO> crear(@RequestBody ServicioDTO nuevo) {
        ServicioDTO creado = servicioService.crear(nuevo);
        return ResponseEntity.ok(creado);
    }
    
    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("servicios", servicioService.listarTodos());
        return "servicios";
    }

}