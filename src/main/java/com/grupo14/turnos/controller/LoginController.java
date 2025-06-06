package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // cerrar sesión
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
        @RequestParam String email,
        @RequestParam String contrasena,
        HttpSession session,
        Model model
    ) {
        try {
            UsuarioDTO usuario = usuarioService.login(email, contrasena);
            session.setAttribute("usuario", usuario);

            if ("CLIENTE".equalsIgnoreCase(usuario.rol())) {
                return "redirect:/cliente/menu";
            } else if ("EMPLEADO".equalsIgnoreCase(usuario.rol())) {
                return "redirect:/empleado/menu";
            } else {
                model.addAttribute("error", "Rol desconocido.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Credenciales inválidas.");
            return "login";
        }
    }
}
