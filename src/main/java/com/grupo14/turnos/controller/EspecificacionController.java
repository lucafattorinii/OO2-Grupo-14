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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("especificaciones", especificacionService.listarTodos());
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("rubros", Rubro.values());
        model.addAttribute("direcciones", direccionService.listarTodos());
        return "especificaciones";
    }


    // 2) FORM-SUBMIT: Crea una especificación y vuelve al listado
    @PostMapping("/create")
    public String crear(@ModelAttribute EspecificacionDTO especificacionDTO, RedirectAttributes redirectAttributes) {
        try {
        	especificacionService.crear(especificacionDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Especificación creada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear especificación: " + e.getMessage());
        }
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
