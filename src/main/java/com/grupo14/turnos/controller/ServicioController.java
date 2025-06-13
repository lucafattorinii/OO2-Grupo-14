package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.MediaType;
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

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verServicios(Model model) {
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<PrestadorDTO> prestadores = prestadorService.listarTodos();
        model.addAttribute("servicios", servicios);
        model.addAttribute("prestadores", prestadores);
        return "servicios";        // templates/servicios.html
    }

    // 2) FORM-SUBMIT: Crea un servicio y vuelve al listado
    @PostMapping("/create")
    public String crearServicio(
            @RequestParam String nombre,
            @RequestParam int duracionMin,
            @RequestParam double precio,
            @RequestParam Long prestadorId
    ) {
        ServicioDTO nuevo = new ServicioDTO(
            null, nombre, duracionMin, precio, prestadorId
        );
        servicioService.crear(nuevo);
        return "redirect:/servicios/view";
    }
    
    @PostMapping("/delete")
    public String eliminarServicio(@RequestParam Long id) {
        servicioService.eliminar(id);
        return "redirect:/servicios/view";
    }

    @PostMapping("/update")
    public String modificarServicio(
        @RequestParam Long id,
        @RequestParam String nombre,
        @RequestParam Integer duracionMin,
        @RequestParam Double precio,
        @RequestParam Integer prestadorId
    ) {
        servicioService.actualizarServicio(id, nombre, duracionMin, precio, prestadorId);
        return "redirect:/servicios/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ServicioDTO> listarTodosJson() {
        return servicioService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServicioDTO obtenerJson(@PathVariable Long id) {
        return servicioService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServicioDTO crearJson(@RequestBody ServicioDTO nuevo) {
        return servicioService.crear(nuevo);
    }
}

