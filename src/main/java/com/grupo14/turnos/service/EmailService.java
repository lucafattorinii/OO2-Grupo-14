package com.grupo14.turnos.service;

import java.util.Map;

/**
 * Servicio para el envío de emails.
 */
public interface EmailService {
    
    /**
     * Envía un email con texto plano.
     * 
     * @param destinatario Dirección de email del destinatario
     * @param asunto Asunto del email
     * @param contenido Contenido del email en texto plano
     */
    void enviarEmailSimple(String destinatario, String asunto, String contenido);
    
    /**
     * Envía un email con contenido HTML utilizando una plantilla Thymeleaf.
     * 
     * @param destinatario Dirección de email del destinatario
     * @param asunto Asunto del email
     * @param plantillaHtml Nombre de la plantilla HTML (sin extensión)
     * @param variables Variables para la plantilla
     */
    void enviarEmailHtml(String destinatario, String asunto, String plantillaHtml, Map<String, Object> variables);
}

