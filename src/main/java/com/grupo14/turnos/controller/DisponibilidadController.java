package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.service.DisponibilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/disponibilidades")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

   /* @GetMapping
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
    */
    
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("disponibilidades", disponibilidadService.listarTodos());
        return "disponibilidades";
    }

    @PostMapping("/guardar")
    public String guardarDisponibilidad(@ModelAttribute DisponibilidadDTO dto, Model model) {
        try {
            disponibilidadService.crear(dto);
            model.addAttribute("mensaje", "Disponibilidad creada correctamente");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("disponibilidades", disponibilidadService.listarTodos());
        return "disponibilidades";
    }

}