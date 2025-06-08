package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.dto.EmpleadoDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.EmpleadoService;
import com.grupo14.turnos.service.ServicioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleado")
public class EmpleadoMenuController {

    private final EmpleadoService empleadoService;
    private final DisponibilidadService disponibilidadService;
    private final ServicioService servicioService;

    public EmpleadoMenuController(EmpleadoService empleadoService, DisponibilidadService disponibilidadService, ServicioService servicioService) {
        this.empleadoService = empleadoService;
        this.disponibilidadService = disponibilidadService;
        this.servicioService = servicioService;
    }

    @GetMapping("/menu")
    public String menuEmpleado(HttpSession session, Model model, @RequestParam(required = false) String editado) {
        String vista = "redirect:/login";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            if ("true".equals(editado)) {
                model.addAttribute("exito", "Datos actualizados correctamente.");
            }
            vista = "empleado/menu";
        }

        return vista;
    }

    @GetMapping("/editar")
    public String mostrarFormularioEdicion(HttpSession session, Model model) {
        String vista = "redirect:/login";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario != null) {
            EmpleadoDTO empleado = empleadoService.obtenerPorId(usuario.id());
            model.addAttribute("empleado", empleado);
            vista = "empleado/editar";
        }

        return vista;
    }

    @PostMapping("/editar")
    public String procesarFormularioEdicion(@Valid @ModelAttribute("empleado") EmpleadoDTO dto, BindingResult result, HttpSession session, Model model) {
        String vista = "redirect:/login";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario != null) {
            if (result.hasErrors()) {
                model.addAttribute("error", "Revisá los campos e intentá de nuevo.");
                vista = "empleado/editar";
            } else {
                try {
                    empleadoService.actualizar(usuario.id(), dto);
                    vista = "redirect:/empleado/menu?editado=true";
                } catch (Exception e) {
                    model.addAttribute("error", "Error al guardar los cambios.");
                    vista = "empleado/editar";
                }
            }
        }

        return vista;
    }
    
    @GetMapping("/crear-disponibilidad")
    public String mostrarFormularioDisponibilidad(HttpSession session, Model model) {
        String vista = "redirect:/login";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("disponibilidad", new DisponibilidadDTO(
                    null, null, null, null, null, usuario.id()
            ));
            model.addAttribute("diasSemana", disponibilidadService.listarDiasSemana());
            model.addAttribute("servicios", servicioService.listarTodos());
            vista = "empleado/disponibilidad";
        }

        return vista;
    }

    @PostMapping("/crear-disponibilidad")
    public String procesarFormularioDisponibilidad(
            @ModelAttribute("disponibilidad") DisponibilidadDTO dto,
            HttpSession session,
            Model model
    ) {
        String vista = "redirect:/login";
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario != null) {
            boolean exito = crearDisponibilidad(dto, usuario.id());

            model.addAttribute("diasSemana", disponibilidadService.listarDiasSemana());
            model.addAttribute("servicios", servicioService.listarTodos());

            if (exito) {
                model.addAttribute("exito", "Disponibilidad agregada correctamente.");
                model.addAttribute("disponibilidad", new DisponibilidadDTO(null, null, null, null, null, usuario.id()));
            } else {
                model.addAttribute("error", "Error al guardar la disponibilidad.");
                model.addAttribute("disponibilidad", dto);
            }

            vista = "empleado/disponibilidad";
        }

        return vista;
    }

    private boolean crearDisponibilidad(DisponibilidadDTO dto, Integer empleadoId) {
        boolean exito = false;

        try {
            DisponibilidadDTO nueva = new DisponibilidadDTO(
                    dto.id(),
                    dto.diaSemana(),
                    dto.horaInicio(),
                    dto.horaFin(),
                    dto.servicioId(),
                    empleadoId
            );

            disponibilidadService.crear(nueva);
            exito = true;
        } catch (Exception e) {
        }

        return exito;
    }
}