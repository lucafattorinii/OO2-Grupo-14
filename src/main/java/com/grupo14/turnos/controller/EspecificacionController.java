package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.modelo.Rubro;
import com.grupo14.turnos.service.DireccionService;
import com.grupo14.turnos.service.EspecificacionService;
import com.grupo14.turnos.service.ServicioService;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;

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

    // 1) Vista HTML: Listado + formulario
    @GetMapping("/view")
    public String viewEspecificaciones(Model model,
                                      @ModelAttribute("exito") String exito,
                                      @ModelAttribute("error") String error) {
        List<EspecificacionDTO> especificaciones = especificacionService.listarTodos();
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<Rubro> rubros = List.of(Rubro.values());
        List<?> direcciones = direccionService.listarTodos();

        model.addAttribute("especificaciones", especificaciones);
        model.addAttribute("servicios", servicios);
        model.addAttribute("rubros", rubros);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("exito", exito);
        model.addAttribute("error", error);

        return "especificaciones";
    }

    // 2) Crear especificación con validación y mensajes
    @PostMapping("/create")
    public String crear(@RequestParam Long servicioId,
                        @RequestParam Rubro rubro,
                        @RequestParam String detalles,
                        @RequestParam Long direccionId,
                        RedirectAttributes redirectAttributes) {

        ServicioDTO servicio = servicioService.obtenerPorId(servicioId);
        if (servicio == null) {
            redirectAttributes.addFlashAttribute("error", "Servicio no encontrado");
            return "redirect:/especificaciones/view";
        }

        boolean direccionExiste = direccionService.existePorId(direccionId);
        if (!direccionExiste) {
            redirectAttributes.addFlashAttribute("error", "Dirección no encontrada");
            return "redirect:/especificaciones/view";
        }

        EspecificacionDTO dto = new EspecificacionDTO(
            null,
            servicioId,
            rubro,
            detalles,
            direccionId,
            servicio.nombre(),
            null
        );

        try {
            especificacionService.crear(dto);
            redirectAttributes.addFlashAttribute("exito", "Especificación creada correctamente");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error al crear especificación: " + ex.getMessage());
        }

        return "redirect:/especificaciones/view";
    }

    // 3) Eliminar especificación
    @PostMapping("/delete")
    public String eliminar(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            especificacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Especificación eliminada correctamente");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar especificación: " + ex.getMessage());
        }
        return "redirect:/especificaciones/view";
    }

    // 4) Actualizar especificación con validación y mensajes
    @PostMapping("/update")
    public String actualizar(@RequestParam Long id,
                             @RequestParam Long servicioId,
                             @RequestParam Rubro rubro,
                             @RequestParam String detalles,
                             @RequestParam Long direccionId,
                             RedirectAttributes redirectAttributes) {

        ServicioDTO servicio = servicioService.obtenerPorId(servicioId);
        if (servicio == null) {
            redirectAttributes.addFlashAttribute("error", "Servicio no encontrado");
            return "redirect:/especificaciones/view";
        }

        boolean direccionExiste = direccionService.existePorId(direccionId);
        if (!direccionExiste) {
            redirectAttributes.addFlashAttribute("error", "Dirección no encontrada");
            return "redirect:/especificaciones/view";
        }

        EspecificacionDTO dto = new EspecificacionDTO(
            id,
            servicioId,
            rubro,
            detalles,
            direccionId,
            servicio.nombre(),
            null
        );

        try {
            especificacionService.actualizar(dto);
            redirectAttributes.addFlashAttribute("exito", "Especificación actualizada correctamente");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar especificación: " + ex.getMessage());
        }

        return "redirect:/especificaciones/view";
    }

    // 5) API REST: listar todos (DTO)
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EspecificacionDTO> listarTodosJson() {
        return especificacionService.listarTodos();
    }

    // 6) API REST: obtener por ID (DTO, no entidad)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EspecificacionDTO obtenerJson(@PathVariable Long id) {
        return especificacionService.buscarPorId(id)
            .map(e -> new EspecificacionDTO(
                e.getId(),
                e.getServicio().getIdServicio(),
                e.getRubro(),
                e.getDetalles(),
                e.getDireccion() != null ? e.getDireccion().getIdDireccion() : null,
                e.getServicio().getNombre(),
                e.getDireccion() != null ? e.getDireccion().getCalle() + " " + e.getDireccion().getNumeroCalle() : null
            ))
            .orElseThrow(() -> new RecursoNoEncontradoException("Especificación no encontrada: " + id));
    }

    // 7) API REST: crear desde JSON
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void crearJson(@RequestBody EspecificacionDTO nueva) {
        especificacionService.crear(nueva);
    }
}
