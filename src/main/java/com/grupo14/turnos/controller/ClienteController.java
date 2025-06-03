package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.modelo.Cliente;
import com.grupo14.turnos.service.ClienteService;
import org.springframework.http.ResponseEntity;
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

    /*@GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(@RequestBody ClienteDTO nuevo) {
        ClienteDTO creado = clienteService.crear(nuevo);
        return ResponseEntity.ok(creado);
    }
    
    @GetMapping("/view")
    public String verClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.listarTodos();

        for (ClienteDTO cliente : clientes) {
            System.out.println("ClienteDTO - ID: " + cliente.id() +
                               ", Nombre: " + cliente.nombre() +
                               ", Apellido: " + cliente.apellido() +
                               ", DNI: " + cliente.dni() +
                               ", Email: " + cliente.email());
        }

        model.addAttribute("clientes", clientes);
        return "clientes";
    }
    
    @GetMapping("/test")
    public String testThymeleaf(Model model) {
        List<ClienteDTO> clientes = List.of(
            new ClienteDTO(3, "correo1@example.com", "pass1", 1001L, 30123456L, "Juan", "Perez"),
            new ClienteDTO(4, "correo2@example.com", "pass2", 1002L, 40123456L, "Ana", "Garcia")
        );

        model.addAttribute("clientes", clientes);
        return "test";  
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute ClienteDTO cliente, Model model) {
        try {
            clienteService.crear(cliente);
            model.addAttribute("mensaje", "Cliente creado correctamente");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes";
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes"; // ← debe existir clientes.html
    }


}