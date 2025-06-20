package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.service.ClienteService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // 1) VISTA HTML: Listado + Formulario
    @GetMapping("/view")
    public String verClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        return "clientes";        // templates/clientes.html
    }

    // 2) FORM-SUBMIT: Crea un cliente y vuelve al listado
    @PostMapping("/create")
    public String crearCliente(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam Long numeroCliente,
            @RequestParam Long dni,
            @RequestParam String nombre,
            @RequestParam String apellido
    ) {
        ClienteDTO nuevo = new ClienteDTO(
            null, email, contrasena, numeroCliente, dni, nombre, apellido
        );
        clienteService.crear(nuevo);
        return "redirect:/clientes/view";
    }
    
    @PostMapping("/delete")
    public String eliminarCliente(@RequestParam Integer id) {
        clienteService.eliminarPorId(id);
        return "redirect:/clientes/view";
    }


    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ClienteDTO> listarTodosJson() {
        return clienteService.listarTodos();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteDTO obtenerJson(@PathVariable Integer id) {
        return clienteService.obtenerPorId(id);
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE,
                          produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ClienteDTO crearJson(@RequestBody ClienteDTO nuevo) {
        return clienteService.crear(nuevo);
    }
    
    @PostMapping("/update")
    public String modificarCliente(
        @RequestParam Integer id,
        @RequestParam String email,
        @RequestParam String contrasena,
        @RequestParam Long dni,
        @RequestParam String nombre,
        @RequestParam String apellido
    ) {
        clienteService.actualizarCliente(id, email, contrasena
                                         , dni, nombre, apellido);
        return "redirect:/clientes/view";
    }
}
