package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.service.DireccionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/direcciones")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verDirecciones(Model model) {
        List<DireccionDTO> direcciones = direccionService.listarTodos();
        model.addAttribute("direcciones", direcciones);
        return "direcciones"; // templates/direcciones.html
    }

    // 2) FORM-SUBMIT: Crear nueva dirección
    @PostMapping("/create")
    public String crearDireccion(
            @RequestParam String pais,
            @RequestParam String provincia,
            @RequestParam String ciudad,
            @RequestParam String calle,
            @RequestParam String numeroCalle,
            @RequestParam String codigoPostal
    ) {
        DireccionDTO nueva = new DireccionDTO(
                null, pais, provincia, ciudad, calle, numeroCalle, codigoPostal
        );
        direccionService.crear(nueva);
        return "redirect:/direcciones/view";
    }

    // 3) FORM-SUBMIT: Eliminar dirección
    @PostMapping("/delete")
    public String eliminarDireccion(@RequestParam Long id) {
        direccionService.eliminar(id);
        return "redirect:/direcciones/view";
    }

    // 4) FORM-SUBMIT: Modificar dirección
    @PostMapping("/update")
    public String modificarDireccion(
            @RequestParam Long id,
            @RequestParam String pais,
            @RequestParam String provincia,
            @RequestParam String ciudad,
            @RequestParam String calle,
            @RequestParam String numeroCalle,
            @RequestParam String codigoPostal
    ) {
        DireccionDTO dto = new DireccionDTO(id, pais, provincia, ciudad, calle, numeroCalle, codigoPostal);
        direccionService.actualizar(id, dto);
        return "redirect:/direcciones/view";
    }

    // API REST: Listar todas
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DireccionDTO> listarTodosJson() {
        return direccionService.listarTodos();
    }

    // API REST: Obtener por ID
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DireccionDTO obtenerJson(@PathVariable Long id) {
        return direccionService.obtenerPorId(id);
    }

    // API REST: Crear dirección (JSON)
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DireccionDTO crearJson(@RequestBody DireccionDTO nueva) {
        return direccionService.crear(nueva);
    }
}
