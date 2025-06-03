package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.service.ClienteService;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.ServicioService;
import com.grupo14.turnos.service.TurnoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/turnos")
public class TurnoController {
    private final TurnoService turnoService;
    
    private final ClienteService clienteService;
    private final ServicioService servicioService;
    private final DisponibilidadService disponibilidadService;

    public TurnoController(
        TurnoService turnoService,
        ClienteService clienteService,
        ServicioService servicioService,
        DisponibilidadService disponibilidadService
    ) {
        this.turnoService = turnoService;
        this.clienteService = clienteService;
        this.servicioService = servicioService;
        this.disponibilidadService = disponibilidadService;
    }


    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listarTodos() {
        return ResponseEntity.ok(turnoService.listarTodos());
    }
    
    @GetMapping("/form")
    public String mostrarFormulario(Model model) {
    	model.addAttribute("turno", new TurnoDTO(null,null,null,"PENDIENTE",null,null,null));
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("disponibilidades", disponibilidadService.listarTodos());
        return "form"; // ← este nombre es clave
    }
    
    @PostMapping("/nuevo")
    public String procesarFormulario(@ModelAttribute @Valid TurnoDTO turno, Model model) {
        try {
            turnoService.crear(turno);
            return "redirect:/turnos/view";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "form";
        }
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