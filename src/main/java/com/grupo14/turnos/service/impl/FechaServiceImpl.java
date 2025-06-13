package com.grupo14.turnos.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo14.turnos.dto.FechaDTO;
import com.grupo14.turnos.modelo.DiaSemana;
import com.grupo14.turnos.modelo.Direccion;
import com.grupo14.turnos.modelo.Fecha;
import com.grupo14.turnos.repository.DireccionRepository;
import com.grupo14.turnos.repository.FechaRepository;
import com.grupo14.turnos.service.FechaService;

@Service
public class FechaServiceImpl implements FechaService {

    @Autowired
    private FechaRepository fechaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    public void crear(FechaDTO dto) {
        Direccion direccion = direccionRepository.findById(dto.direccionId())
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        Fecha fecha = new Fecha();
        fecha.setFecha(dto.fecha());
        fecha.setDiaSemana(dto.diaSemana());
        fecha.setDireccion(direccion);

        fechaRepository.save(fecha);
    }
    
    @Override
    public Fecha crear(LocalDate date, Long direccionId) {
        Direccion direccion = direccionRepository.findById(direccionId)
            .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        Fecha fecha = new Fecha();
        fecha.setFecha(date);
        fecha.setDiaSemana(convertirADiaSemana(date));
        fecha.setDireccion(direccion);

        return fechaRepository.save(fecha);
    }

    @Override
    public FechaDTO obtenerPorId(Long id) {
        return fechaRepository.findById(id)
            .map(f -> new FechaDTO(f.getId(), f.getFecha(), f.getDiaSemana(), f.getDireccion().getIdDireccion()))
            .orElse(null);
    }

    @Override
    public List<FechaDTO> listarTodas() {
        return fechaRepository.findAll().stream()
            .map(f -> new FechaDTO(f.getId(), f.getFecha(), f.getDiaSemana(), f.getDireccion().getIdDireccion()))
            .toList();
    }

    @Override
    public void eliminar(Long id) {
        fechaRepository.deleteById(id);
    }
    
    private DiaSemana convertirADiaSemana(LocalDate fecha) {
        return switch (fecha.getDayOfWeek()) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }
}
