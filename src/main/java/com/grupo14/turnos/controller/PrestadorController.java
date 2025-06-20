package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.service.PrestadorService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/prestadores")
public class PrestadorController {
    private final PrestadorService prestadorService;

    public PrestadorController(PrestadorService prestadorService) {
        this.prestadorService = prestadorService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verPrestadores(Model model) {
        List<PrestadorDTO> prestadores = prestadorService.listarTodos();
        model.addAttribute("prestadores", prestadores);
        return "prestadores";        // templates/prestadores.html
    }

    // 2) FORM-SUBMIT: Crea un prestador y vuelve al listado
    @PostMapping("/create")
    public String crearPrestador(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String razonSocial,
            @RequestParam Boolean habilitado
    ) {
        PrestadorDTO nuevo = new PrestadorDTO(
            null, email, contrasena, razonSocial, habilitado
        );
        prestadorService.crear(nuevo);
        return "redirect:/prestadores/view";
    }
    
    @PostMapping("/delete")
    public String eliminarPrestador(@RequestParam Integer id) {
        prestadorService.eliminar(id);
        return "redirect:/prestadores/view";
    }

    @PostMapping("/update")
    public String modificarPrestador(
        @RequestParam Integer id,
        @RequestParam String email,
        @RequestParam String contrasena,
        @RequestParam String razonSocial,
        @RequestParam Boolean habilitado
    ) {
        prestadorService.actualizarPrestador(id, email, contrasena, razonSocial, habilitado);
        return "redirect:/prestadores/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<PrestadorDTO> listarTodosJson() {
        return prestadorService.listarTodos();
    }
    
    
    
    
    
    
    
    

    

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PrestadorDTO obtenerJson(@PathVariable Integer id) {
        return prestadorService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PrestadorDTO crearJson(@RequestBody PrestadorDTO nuevo) {
        return prestadorService.crear(nuevo);
    }
}

