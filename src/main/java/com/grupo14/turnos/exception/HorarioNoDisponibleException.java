package com.grupo14.turnos.exception;

public class HorarioNoDisponibleException extends RuntimeException {
    public HorarioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
