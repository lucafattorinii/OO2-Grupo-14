package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.dto.TurnoConFechaDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.modelo.EstadoTurno;
import com.grupo14.turnos.service.ClienteService;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.ServicioService;
import com.grupo14.turnos.service.TurnoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verTurnos(Model model) {
        List<TurnoDTO> turnos = turnoService.listarTodos();
        List<ClienteDTO> clientes = clienteService.listarTodos();
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<DisponibilidadDTO> disponibilidades = disponibilidadService.listarTodos();
        List<EstadoTurno> estados = turnoService.listarEstados();
        
        model.addAttribute("turnos", turnos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("servicios", servicios);
        model.addAttribute("disponibilidades", disponibilidades);
        model.addAttribute("estados", estados);
        
        return "turnos";        // templates/turnos.html
    }

    // 2) FORM-SUBMIT: Crea un turno y vuelve al listado
    @PostMapping("/create")
    public String crearTurno(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,  
            @RequestParam Long direccionId,   
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            @RequestParam String estado,
            @RequestParam Long clienteId,
            @RequestParam Long disponibilidadId,
            @RequestParam Long servicioId
    ) {
        TurnoConFechaDTO nuevo = new TurnoConFechaDTO(
            fecha,
            hora,
            EstadoTurno.valueOf(estado.toUpperCase()),
            clienteId,
            disponibilidadId,
            servicioId,
            direccionId
        );
        turnoService.crear(nuevo);
        return "redirect:/turnos/view";
    }
    
    @PostMapping("/delete")
    public String eliminarTurno(@RequestParam Integer id) {
        turnoService.eliminar(id);
        return "redirect:/turnos/view";
    }

    @PostMapping("/update")
    public String modificarTurno(
        @RequestParam Long id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
        @RequestParam Long direccionId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
        @RequestParam String estado,
        @RequestParam Long clienteId,
        @RequestParam Long disponibilidadId,
        @RequestParam Long servicioId
    ) {
        TurnoConFechaDTO dto = new TurnoConFechaDTO(
            fecha,
            hora,
            EstadoTurno.valueOf(estado.toUpperCase()),
            clienteId,
            disponibilidadId,
            servicioId,
            direccionId
        );
        turnoService.actualizar(id, dto);
        return "redirect:/turnos/view";
    }
    
    @PostMapping("/cambiar-estado")
    public String cambiarEstado(
        @RequestParam Integer id,
        @RequestParam String estado
    ) {
        turnoService.cambiarEstado(id, estado);
        return "redirect:/turnos/view";
    }
    
    // Página para crear un turno desde el calendario
    @GetMapping("/crear")
    public String crearTurnoDesdeCalendario(
        @RequestParam String fecha,
        @RequestParam String hora,
        @RequestParam Integer empleadoId,
        @RequestParam Long servicioId,
        Model model
    ) {
        List<ClienteDTO> clientes = clienteService.listarTodos();
        ServicioDTO servicio = servicioService.obtenerPorId(servicioId);
        
        model.addAttribute("fecha", fecha);
        model.addAttribute("hora", hora);
        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("servicioId", servicioId);
        model.addAttribute("servicio", servicio);
        model.addAttribute("clientes", clientes);
        
        return "crear-turno";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TurnoDTO> listarTodosJson() {
        return turnoService.listarTodos();
    }

    @GetMapping(path = "/estado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TurnoDTO> listarPorEstadoJson(@PathVariable String estado) {
        return turnoService.listarPorEstado(estado);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public TurnoConFechaDTO crearJson(@RequestBody TurnoConFechaDTO nuevo) {
        return turnoService.crear(nuevo);
    }
}

