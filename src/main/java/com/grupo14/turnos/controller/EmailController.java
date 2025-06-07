package com.grupo14.turnos.controller;

import com.grupo14.turnos.service.EmailService;

import ch.qos.logback.core.model.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email/enviar")
    public String mostrarFormularioEmail() {
        return "email/enviar";
    }

    @PostMapping("/email/enviar")
    public String enviarEmail(@RequestParam("to") String to,
                              @RequestParam("subject") String subject,
                              @RequestParam("body") String body,
                              ModelMap model) {
        try {
            emailService.enviarEmail(to, subject, body);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "email/enviar";
    }
}
