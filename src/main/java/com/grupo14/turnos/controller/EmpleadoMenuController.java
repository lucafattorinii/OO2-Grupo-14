package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.EmpleadoDTO;
import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.EmpleadoService;
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

    public EmpleadoMenuController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
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
    public String procesarFormularioEdicion(@Valid @ModelAttribute("empleado") EmpleadoDTO dto,
                                            BindingResult result,
                                            HttpSession session,
                                            Model model) {
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
}