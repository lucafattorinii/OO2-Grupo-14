package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.modelo.DiaSemana;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.EmpleadoService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;
    private final ServicioService servicioService;

    public DisponibilidadController(
        DisponibilidadService disponibilidadService,
        ServicioService servicioService
    ) {
        this.disponibilidadService = disponibilidadService;
        this.servicioService = servicioService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verDisponibilidades(Model model) {
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos();
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<DiaSemana> diasSemana = disponibilidadService.listarDiasSemana();

        model.addAttribute("disponibilidades", disponibilidades);
        model.addAttribute("servicios", servicios);
        model.addAttribute("diasSemana", diasSemana);

        return "disponibilidades"; // templates/disponibilidades.html
    }

    // 2) FORM-SUBMIT: Crear nueva disponibilidad
    @PostMapping("/create")
    public String crearDisponibilidad(
            @RequestParam String diaSemana,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam List<Long> servicioIds
    ) {
        DisponibilidadDTO nueva = new DisponibilidadDTO(
                null,
                DiaSemana.valueOf(diaSemana.toUpperCase()),
                LocalTime.parse(horaInicio),
                LocalTime.parse(horaFin),
                new HashSet<>(servicioIds)
        );
        disponibilidadService.crear(nueva);
        return "redirect:/disponibilidades/view";
    }

    // 3) FORM-SUBMIT: Eliminar disponibilidad
    @PostMapping("/delete")
    public String eliminarDisponibilidad(@RequestParam Long id) {
        disponibilidadService.eliminar(id);
        return "redirect:/disponibilidades/view";
    }

    // 4) FORM-SUBMIT: Modificar disponibilidad
    @PostMapping("/update")
    public String modificarDisponibilidad(
            @RequestParam Long id,
            @RequestParam String diaSemana,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam List<Long> servicioIds
    ) {
        DisponibilidadDTO dto = new DisponibilidadDTO(
                id,
                DiaSemana.valueOf(diaSemana.toUpperCase()),
                LocalTime.parse(horaInicio),
                LocalTime.parse(horaFin),
                new HashSet<>(servicioIds)
        );
        disponibilidadService.actualizar(id, dto);
        return "redirect:/disponibilidades/view";
    }

    // 5) API REST: Listar todos
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DisponibilidadDTO> listarTodosJson() {
        return disponibilidadService.listarTodos();
    }

    // 6) API REST: Obtener por ID
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DisponibilidadDTO obtenerJson(@PathVariable Long id) {
        return disponibilidadService.obtenerPorId(id);
    }

    // 7) API REST: Crear desde JSON
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DisponibilidadDTO crearJson(@RequestBody DisponibilidadDTO nueva) {
        return disponibilidadService.crear(nueva);
    }

    // 8) VISTA: Calendario
    @GetMapping("/calendario")
    public String verCalendario(Model model) {
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos();
        model.addAttribute("disponibilidades", disponibilidades);
        return "calendario-disponibilidades";
    }
}

