package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServicioController {
    private final ServicioService servicioService;
    private final PrestadorService prestadorService;

    public ServicioController(ServicioService servicioService, PrestadorService prestadorService) {
        this.servicioService = servicioService;
        this.prestadorService = prestadorService;
    }

    /*@GetMapping
    public ResponseEntity<List<ServicioDTO>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }*/
    
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("prestadores", prestadorService.listarTodos());
        return "servicios";
    }

    
    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute ServicioDTO servicio, Model model) {
        try {
            servicioService.crear(servicio);
            model.addAttribute("mensaje", "Servicio creado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("servicios", servicioService.listarTodos());
        return "servicios";
    }

    /*@GetMapping("/{id}")
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
    } */

}