package com.grupo14.turnos.controller;

import com.grupo14.turnos.dto.ClienteDTO;
import com.grupo14.turnos.dto.TurnoDTO;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import com.grupo14.turnos.service.ClienteService;
import com.grupo14.turnos.service.EmailService;
import com.grupo14.turnos.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/view")
public class ViewController {
    
    private final ClienteService clienteService;
    private final TurnoService turnoService;
    private final EmailService emailService;

    @Autowired
    public ViewController(ClienteService clienteService, TurnoService turnoService, EmailService emailService) {
        this.clienteService = clienteService;
        this.turnoService = turnoService;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String index() {
        return "menu";
    }

    @GetMapping("/clientes")
    public String verClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.listarTodos();
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
    
    @GetMapping("/turnos")
    public String verTurnos(Model model) {
        List<TurnoDTO> turnos = turnoService.listarTodos();
        model.addAttribute("turnos", turnos);
        return "turnos";
    }
    
    @GetMapping("/error-test")
    public String testError() {
        
        throw new RecursoNoEncontradoException("Este es un error de prueba para verificar el manejador de excepciones");
    }
    
    @GetMapping("/email-test/{clienteId}")
    public String testEmail(@PathVariable Integer clienteId, Model model) {
        try {
            ClienteDTO cliente = clienteService.obtenerPorId(clienteId);
            
            // Enviar un email de prueba
            Map<String, Object> variables = new HashMap<>();
            variables.put("nombreCliente", cliente.nombre() + " " + cliente.apellido());
            variables.put("servicio", "Servicio de Prueba");
            variables.put("fecha", "01/01/2025");
            variables.put("hora", "10:00");
            variables.put("prestador", "Prestador de Prueba");
            
            emailService.enviarEmailHtml(
                cliente.email(),
                "Confirmación de Turno de Prueba",
                "email/confirmacion-turno",
                variables
            );
            
            model.addAttribute("mensaje", "Email enviado correctamente a " + cliente.email());
            return "mensaje";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al enviar email: " + e.getMessage());
            return "mensaje";
        }
    }
}

