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
        return "direcciones";        // templates/direcciones.html
    }

    // 2) FORM-SUBMIT: Crea una dirección y vuelve al listado
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
    
    @PostMapping("/delete")
    public String eliminarDireccion(@RequestParam Integer id) {
        direccionService.eliminar(id);
        return "redirect:/direcciones/view";
    }

    @PostMapping("/update")
    public String modificarDireccion(
        @RequestParam Integer id,
        @RequestParam String pais,
        @RequestParam String provincia,
        @RequestParam String ciudad,
        @RequestParam String calle,
        @RequestParam String numeroCalle,
        @RequestParam String codigoPostal
    ) {
        direccionService.actualizarDireccion(id, pais, provincia, ciudad, calle, numeroCalle, codigoPostal);
        return "redirect:/direcciones/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DireccionDTO> listarTodosJson() {
        return direccionService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DireccionDTO obtenerJson(@PathVariable Integer id) {
        return direccionService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DireccionDTO crearJson(@RequestBody DireccionDTO nueva) {
        return direccionService.crear(nueva);
    }
}

