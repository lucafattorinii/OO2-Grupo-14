package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.service.PrestadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prestadores")
public class PrestadorController {

    private final PrestadorService prestadorService;

    public PrestadorController(PrestadorService prestadorService) {
        this.prestadorService = prestadorService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("prestadores", prestadorService.listarTodos());
        return "prestadores";
    }

    @PostMapping("/guardar")
    public String guardarPrestador(@ModelAttribute PrestadorDTO dto, Model model) {
        try {
            prestadorService.crear(dto);
            model.addAttribute("mensaje", "Prestador creado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("prestadores", prestadorService.listarTodos());
        return "prestadores";
    }
}
