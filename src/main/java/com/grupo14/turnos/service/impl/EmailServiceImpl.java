package com.grupo14.turnos.service.impl;

import com.grupo14.turnos.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

/**
 * Implementación del servicio de email.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void enviarEmailSimple(String destinatario, String asunto, String contenido) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(contenido);
        mailSender.send(mensaje);
    }

    @Override
    public void enviarEmailHtml(String destinatario, String asunto, String plantillaHtml, Map<String, Object> variables) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }
            
            String contenidoHtml = templateEngine.process(plantillaHtml, context);
            
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);
            
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar email HTML", e);
        }
    }
}

