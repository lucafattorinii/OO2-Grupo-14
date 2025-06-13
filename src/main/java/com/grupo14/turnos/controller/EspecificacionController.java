package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.modelo.Especificacion;
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
import java.util.Optional;

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
    public String viewEspecificaciones(Model model) {
        List<EspecificacionDTO> especificaciones = especificacionService.listarTodos();
        List<ServicioDTO> servicios = servicioService.listarTodos();
        List<Rubro> rubros = List.of(Rubro.values());

        List<DireccionDTO> direcciones = direccionService.listarTodos();

        model.addAttribute("especificaciones", especificaciones);
        model.addAttribute("servicios", servicios);
        model.addAttribute("rubros", rubros);
        model.addAttribute("direcciones", direcciones);

        return "especificaciones";
    }


    // 2) FORM-SUBMIT: Crea una especificación y vuelve al listado
    @PostMapping("/create")
    public String crear(@RequestParam Long servicioId,
                        @RequestParam Rubro rubro,
                        @RequestParam String detalles,
                        @RequestParam Long direccionId) {

        ServicioDTO servicio = servicioService.obtenerPorId(servicioId);
        String servicioNombre = (servicio != null) ? servicio.nombre() : "Desconocido";

        EspecificacionDTO dto = new EspecificacionDTO(
            null,
            servicioId,
            rubro,
            detalles,
            direccionId,
            servicioNombre,
            null  // Podés completar `direccionTexto` más adelante si hace falta
        );

        especificacionService.crear(dto);
        return "redirect:/especificaciones/view";
    }

    
    @PostMapping("/delete")
    public String eliminar(@RequestParam Integer id) {
        especificacionService.eliminar(id);
        return "redirect:/especificaciones/view";
    }

    @PostMapping("/update")
    public String actualizar(@RequestParam Long id,
                             @RequestParam Long servicioId,
                             @RequestParam Rubro rubro,
                             @RequestParam String detalles,
                             @RequestParam Long direccionId) {

        ServicioDTO servicio = servicioService.obtenerPorId(servicioId);
        String servicioNombre = (servicio != null) ? servicio.nombre() : "Desconocido";

        EspecificacionDTO dto = new EspecificacionDTO(
            id,
            servicioId,
            rubro,
            detalles,
            direccionId,
            servicioNombre,
            null // direcciónTexto, si hace falta podés recuperarlo desde otro servicio
        );

        especificacionService.actualizar(dto);
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
    public Optional<Especificacion> obtenerJson(@PathVariable Integer id) {
        return especificacionService.buscarPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public void crearJson(@RequestBody EspecificacionDTO nueva) {
especificacionService.crear(nueva);
}

}
