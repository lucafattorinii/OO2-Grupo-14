package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.HttpStatus;
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
    public String listarServicios(Model model) {
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<PrestadorDTO> prestadores = prestadorService.listarTodos();
        model.addAttribute("servicios", servicios);
        model.addAttribute("prestadores", prestadores);
        return "servicios";
    }

    // 2) FORM-SUBMIT: Crear servicio
    @PostMapping("/create")
    public String crearServicio(
            @RequestParam String nombre,
            @RequestParam int duracionMin,
            @RequestParam double precio,
            @RequestParam Long prestadorId
    ) {
        ServicioDTO nuevo = new ServicioDTO(null, nombre, duracionMin, precio, prestadorId);
        servicioService.crear(nuevo);
        return "redirect:/servicios/view";
    }

    // 3) FORM-SUBMIT: Eliminar servicio
    @PostMapping("/delete")
    public String eliminarServicio(@RequestParam Long id) {
        servicioService.eliminar(id);
        return "redirect:/servicios/view";
    }

    // 4) FORM-SUBMIT: Modificar servicio
    @PostMapping("/update")
    public String actualizarServicio(
            @RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam Integer duracionMin,
            @RequestParam Double precio,
            @RequestParam Long prestadorId
    ) {
        ServicioDTO dto = new ServicioDTO(id, nombre, duracionMin, precio, prestadorId);
        servicioService.actualizar(id, dto);
        return "redirect:/servicios/view";
    }

    // 5) API REST: Listar todos
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ServicioDTO> listarTodosJson() {
        return servicioService.listarTodos();
    }

    // 6) API REST: Obtener uno por ID
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServicioDTO obtenerJson(@PathVariable Long id) {
        return servicioService.obtenerPorId(id);
    }

    // 7) API REST: Crear con JSON
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ServicioDTO crearJson(@RequestBody ServicioDTO nuevo) {
        return servicioService.crear(nuevo);
    }
}