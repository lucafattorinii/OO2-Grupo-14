package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.modelo.Rubro;
import com.grupo14.turnos.service.DireccionService;
import com.grupo14.turnos.service.EspecificacionService;
import com.grupo14.turnos.service.ServicioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/especificaciones")
public class EspecificacionController {

    private final EspecificacionService especificacionService;
    private final ServicioService servicioService;
    private final DireccionService direccionService;

    public EspecificacionController(
        EspecificacionService especificacionService,
        ServicioService servicioService,
        DireccionService direccionService
    ) {
        this.especificacionService = especificacionService;
        this.servicioService = servicioService;
        this.direccionService = direccionService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verEspecificaciones(Model model) {
        List<EspecificacionDTO> especificaciones = especificacionService.listarTodos();
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<DireccionDTO> direcciones = direccionService.listarTodos();
        List<Rubro> rubros = especificacionService.listarRubros();
        
        model.addAttribute("especificaciones", especificaciones);
        model.addAttribute("servicios", servicios);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("rubros", rubros);
        
        return "especificaciones";        // templates/especificaciones.html
    }

    // 2) FORM-SUBMIT: Crea una especificación y vuelve al listado
    @PostMapping("/create")
    public String crearEspecificacion(
            @RequestParam Long servicioId,
            @RequestParam Rubro rubro,
            @RequestParam String detalles,
            @RequestParam Integer direccionId
    ) {
        EspecificacionDTO nueva = new EspecificacionDTO(
            null, servicioId, rubro, detalles, direccionId
        );
        especificacionService.crear(nueva);
        return "redirect:/especificaciones/view";
    }
    
    @PostMapping("/delete")
    public String eliminarEspecificacion(@RequestParam Integer id) {
        especificacionService.eliminar(id);
        return "redirect:/especificaciones/view";
    }

    @PostMapping("/update")
    public String modificarEspecificacion(
        @RequestParam Integer id,
        @RequestParam Long servicioId,
        @RequestParam Rubro rubro,
        @RequestParam String detalles,
        @RequestParam Integer direccionId
    ) {
        especificacionService.actualizarEspecificacion(id, servicioId, rubro, detalles, direccionId);
        return "redirect:/especificaciones/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EspecificacionDTO> listarTodosJson() {
        return especificacionService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EspecificacionDTO obtenerJson(@PathVariable Integer id) {
        return especificacionService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EspecificacionDTO crearJson(@RequestBody EspecificacionDTO nueva) {
        return especificacionService.crear(nueva);
    }
}

