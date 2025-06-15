package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.EmpleadoDTO;
import com.grupo14.turnos.service.EmpleadoService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verEmpleados(Model model) {
        List<EmpleadoDTO> empleados = empleadoService.listarTodos();
        model.addAttribute("empleados", empleados);
        return "empleados";        // templates/empleados.html
    }

    // 2) FORM-SUBMIT: Crea un empleado y vuelve al listado
    @PostMapping("/create")
    public String crearEmpleado(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam Long cuit,
            @RequestParam Integer legajo,
            @RequestParam String puestoCargo,
            @RequestParam Long servicioId // nuevo parámetro
    ) {
        EmpleadoDTO nuevo = new EmpleadoDTO(
            null, email, contrasena, nombre, apellido, dni, cuit, legajo, puestoCargo, servicioId
        );
        empleadoService.crear(nuevo);
        return "redirect:/empleados/view";
    }
    
    @PostMapping("/delete")
    public String eliminarEmpleado(@RequestParam Integer id) {
        empleadoService.eliminar(id);
        return "redirect:/empleados/view";
    }

    @PostMapping("/update")
    public String modificarEmpleado(
        @RequestParam Integer id,
        @RequestParam String email,
        @RequestParam String contrasena,
        @RequestParam Long dni,
        @RequestParam String nombre,
        @RequestParam String apellido,
        @RequestParam Long cuit,
        @RequestParam Integer legajo,
        @RequestParam String puestoCargo
    ) {
        empleadoService.actualizarEmpleado(id, email, contrasena, dni, nombre, apellido, cuit, legajo, puestoCargo);
        return "redirect:/empleados/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<EmpleadoDTO> listarTodosJson() {
        return empleadoService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EmpleadoDTO obtenerJson(@PathVariable Integer id) {
        return empleadoService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public EmpleadoDTO crearJson(@RequestBody EmpleadoDTO nuevo) {
        return empleadoService.crear(nuevo);
    }
}

