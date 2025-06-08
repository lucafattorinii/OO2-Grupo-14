package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prestador")
public class PrestadorMenuController {

    @GetMapping("/menu")
    public String menuPrestador(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login"; // si no hay sesión, redirige a login
        }
        model.addAttribute("usuario", usuario);
        return "prestador/menu"; 
    }
}