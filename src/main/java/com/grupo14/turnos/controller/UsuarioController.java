package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.UsuarioDTO;
import com.grupo14.turnos.service.UsuarioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verUsuarios(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";        // templates/usuarios.html
    }

    // 2) FORM-SUBMIT: Crea un usuario y vuelve al listado
    @PostMapping("/create")
    public String crearUsuario(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String rol
    ) {
        UsuarioDTO nuevo = new UsuarioDTO(
            null, email, contrasena, rol
        );
        usuarioService.crear(nuevo);
        return "redirect:/usuarios/view";
    }
    
    @PostMapping("/delete")
    public String eliminarUsuario(@RequestParam Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios/view";
    }

    @PostMapping("/update")
    public String modificarUsuario(
        @RequestParam Integer id,
        @RequestParam String email,
        @RequestParam(required = false) String contrasena
    ) {
        usuarioService.actualizarUsuario(id, email, contrasena);
        return "redirect:/usuarios/view";
    }

    // API REST endpoints
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UsuarioDTO> listarTodosJson() {
        return usuarioService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UsuarioDTO obtenerJson(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UsuarioDTO crearJson(@RequestBody UsuarioDTO nuevo) {
        return usuarioService.crear(nuevo);
    }
}

