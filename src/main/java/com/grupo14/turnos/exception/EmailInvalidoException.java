package com.grupo14.turnos.exception;

public class EmailInvalidoException extends RuntimeException {
    public EmailInvalidoException(String mensaje) {
        super(mensaje);
    }
}

