package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.dto.EmpleadoDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.modelo.DiaSemana;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.EmpleadoService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/disponibilidades")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;
    private final EmpleadoService empleadoService;
    private final ServicioService servicioService;

    public DisponibilidadController(
        DisponibilidadService disponibilidadService,
        EmpleadoService empleadoService,
        ServicioService servicioService
    ) {
        this.disponibilidadService = disponibilidadService;
        this.empleadoService = empleadoService;
        this.servicioService = servicioService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verDisponibilidades(Model model) {
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos();
        List<EmpleadoDTO> empleados = empleadoService.listarTodos();
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<DiaSemana> diasSemana = disponibilidadService.listarDiasSemana();
        
        model.addAttribute("disponibilidades", disponibilidades);
        model.addAttribute("empleados", empleados);
        model.addAttribute("servicios", servicios);
        model.addAttribute("diasSemana", diasSemana);
        
        return "disponibilidades";        // templates/disponibilidades.html
    }

    // 2) FORM-SUBMIT: Crea una disponibilidad y vuelve al listado
    @PostMapping("/create")
    public String crearDisponibilidad(
            @RequestParam String diaSemana,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam Long servicioId,
            @RequestParam Integer empleadoId
    ) {
        DisponibilidadDTO nueva = new DisponibilidadDTO(
            null, diaSemana, horaInicio, horaFin, servicioId, empleadoId
        );
        disponibilidadService.crear(nueva);
        return "redirect:/disponibilidades/view";
    }
    
    @PostMapping("/delete")
    public String eliminarDisponibilidad(@RequestParam Integer id) {
        disponibilidadService.eliminar(id);
        return "redirect:/disponibilidades/view";
    }

    @PostMapping("/update")
    public String modificarDisponibilidad(
        @RequestParam Integer id,
        @RequestParam String diaSemana,
        @RequestParam String horaInicio,
        @RequestParam String horaFin,
        @RequestParam Long servicioId,
        @RequestParam Integer empleadoId
    ) {
        disponibilidadService.actualizarDisponibilidad(id, diaSemana, horaInicio, horaFin, servicioId, empleadoId);
        return "redirect:/disponibilidades/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DisponibilidadDTO> listarTodosJson() {
        return disponibilidadService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DisponibilidadDTO obtenerJson(@PathVariable Integer id) {
        return disponibilidadService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DisponibilidadDTO crearJson(@RequestBody DisponibilidadDTO nueva) {
        return disponibilidadService.crear(nueva);
    }
    
    // Endpoint para el calendario de disponibilidades
    @GetMapping("/calendario")
    public String verCalendario(Model model) {
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos();
        model.addAttribute("disponibilidades", disponibilidades);
        return "calendario-disponibilidades";
    }
}

