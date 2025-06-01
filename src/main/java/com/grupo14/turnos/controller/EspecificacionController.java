package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.service.EspecificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/especificaciones")
public class EspecificacionController {

    private final EspecificacionService especificacionService;

    public EspecificacionController(EspecificacionService especificacionService) {
        this.especificacionService = especificacionService;
    }

    @GetMapping
    public ResponseEntity<List<EspecificacionDTO>> listarTodos() {
        return ResponseEntity.ok(especificacionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecificacionDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(especificacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EspecificacionDTO> crear(@RequestBody EspecificacionDTO nuevo) {
        return ResponseEntity.ok(especificacionService.crear(nuevo));
    }
    
    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("especificaciones", especificacionService.listarTodos());
        return "especificaciones";
    }

}
