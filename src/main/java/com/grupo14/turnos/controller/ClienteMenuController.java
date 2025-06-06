package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteMenuController {

    @GetMapping("/menu")
    public String menuCliente(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login"; // si no está logueado, lo mando a login
        }
        model.addAttribute("usuario", usuario);
        return "cliente/menu"; // este sería el nombre del template HTML para el menú del cliente
    }
}