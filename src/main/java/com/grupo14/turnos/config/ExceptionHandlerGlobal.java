package com.grupo14.turnos.config;

import com.grupo14.turnos.exception.EmailInvalidoException;
import com.grupo14.turnos.exception.DisponibilidadDuplicadaException;
import com.grupo14.turnos.exception.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Manejador global de excepciones.
 * Retorna vistas HTML para las excepciones.
 */
@ControllerAdvice
public class ExceptionHandlerGlobal {
    
    /**
     * Maneja la excepción RecursoNoEncontradoException.
     * 
     * @param ex Excepción lanzada
     * @param model Modelo para la vista
     * @return Nombre de la vista a renderizar
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RecursoNoEncontradoException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        return "error";
    }
    
    /**
     * Maneja cualquier otra excepción no capturada.
     * 
     * @param ex Excepción lanzada
     * @param model Modelo para la vista
     * @return Nombre de la vista a renderizar
     */
    
    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<String> manejarEmailInvalido(EmailInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    
    
    
    
    
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("mensaje", "Ha ocurrido un error inesperado: " + ex.getMessage());
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error";
    }
    
 // 400 Bad Request si la disponibilidad ya existe
    @ExceptionHandler(DisponibilidadDuplicadaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarDisponibilidadDuplicada(DisponibilidadDuplicadaException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        return "error-disponibilidad"; // redirige a templates/error-disponbilidad.html
    }
}

