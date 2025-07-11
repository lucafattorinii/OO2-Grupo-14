package com.grupo14.turnos.exception;

//Se lanza cuando se intenta registrar una disponibilidad
//idéntica (mismo servicio, día y rango horario) que ya existe.

public class DisponibilidadDuplicadaException extends RuntimeException{
	
	public DisponibilidadDuplicadaException(String mensaje) {
        super(mensaje);
    }

}
