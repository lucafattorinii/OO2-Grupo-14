package com.grupo14.turnos.config;

import com.grupo14.turnos.dto.DireccionDTO;
import com.grupo14.turnos.dto.DisponibilidadDTO;
import com.grupo14.turnos.dto.EspecificacionDTO;
import com.grupo14.turnos.dto.PrestadorDTO;
import com.grupo14.turnos.dto.ServicioDTO;
import com.grupo14.turnos.modelo.DiaSemana;
import com.grupo14.turnos.modelo.Rol;
import com.grupo14.turnos.modelo.Rubro;
import com.grupo14.turnos.service.DireccionService;
import com.grupo14.turnos.service.DisponibilidadService;
import com.grupo14.turnos.service.EspecificacionService;
import com.grupo14.turnos.service.PrestadorService;
import com.grupo14.turnos.service.ServicioService;
import com.grupo14.turnos.service.impl.EspecificacionServiceImpl;
import com.grupo14.turnos.repository.UsuarioRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PrestadorService prestadorService;
    @Autowired private ServicioService servicioService;
    @Autowired private EspecificacionServiceImpl especificacionService;
    @Autowired private DireccionService direccionService;
    @Autowired private DisponibilidadService disponibilidadService;
    @Autowired private PasswordEncoder passwordEncoder;

    private Long prestadorId;
    private DireccionDTO direccion;
    private List<ServicioDTO> serviciosCreados = new ArrayList<>();

    @PostConstruct
    public void init() {
        initAdmin();
        initPrestador();
        initDireccion();
        initServicios();
        initEspecificaciones();
        initDisponibilidades();
    }

    private void initAdmin() {
        if (usuarioRepository.findByEmail("grupo14.tp@gmail.com").isEmpty()) {
            var admin = new com.grupo14.turnos.modelo.Usuario();
            admin.setEmail("grupo14.tp@gmail.com");
            admin.setContrasena(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("✔ Admin creado correctamente.");
        }
    }

    private void initPrestador() {
        if (usuarioRepository.findByEmail("prestador.grupo14@gmail.com").isEmpty()) {
            PrestadorDTO prestadorDTO = new PrestadorDTO(null, "prestador.grupo14@gmail.com", "prestador123", "Prestador de ejemplo", true);
            prestadorId = prestadorService.crear(prestadorDTO).getId();
            System.out.println("✔ Prestador creado correctamente.");
        } else {
            prestadorId = prestadorService.buscarPorEmail("prestador.grupo14@gmail.com").getId();
            System.out.println("✔ Prestador ya existe.");
        }
    }

    private void initDireccion() {
        DireccionDTO direccionDTO = new DireccionDTO(null, "Argentina", "Buenos Aires", "CABA", "Calle Falsa", "123", "1000");
        direccion = direccionService.listarTodos().stream()
            .filter(d -> d.calle().equalsIgnoreCase(direccionDTO.calle()) && d.numeroCalle().equalsIgnoreCase(direccionDTO.numeroCalle()))
            .findFirst()
            .orElseGet(() -> {
                DireccionDTO nueva = direccionService.crear(direccionDTO);
                System.out.println("✔ Dirección creada correctamente.");
                return nueva;
            });
    }

    private void initServicios() {
        String[] nombres = {"Corte Básico", "Peinado Formal", "Tintura Express"};

        for (String nombre : nombres) {
            ServicioDTO existente = servicioService.listarTodos().stream()
                .filter(s -> s.nombre().equalsIgnoreCase(nombre) && s.prestadorId().equals(prestadorId))
                .findFirst()
                .orElse(null);

            ServicioDTO servicio;
            if (existente != null) {
                servicio = existente;
                System.out.println("✔ Servicio '" + nombre + "' ya existe.");
            } else {
                servicio = servicioService.crearServicioSinSincronizar(new ServicioDTO(null, nombre, 30, 2500.0, prestadorId));
                if (servicio.id() == null) {
                    throw new IllegalStateException("El servicio '" + nombre + "' fue creado pero su ID es null.");
                }
                System.out.println("✔ Servicio '" + nombre + "' creado.");
            }
            serviciosCreados.add(servicio);
        }
    }

    private void initEspecificaciones() {
    	
    	System.out.println("→ Servicios cargados: " + serviciosCreados.size());
    	System.out.println("→ Dirección para todas las especificaciones: " + 
    	    (direccion != null ? direccion.idDireccion() : "NULL"));

    	for (ServicioDTO servicio : serviciosCreados) {
    	    System.out.println("→ Preparando especificación para servicio ID: " + servicio.id());}
    	    
        for (ServicioDTO servicio : serviciosCreados) {
            EspecificacionDTO especificacionDTO = new EspecificacionDTO(
                null,
                servicio.id(),
                Rubro.BELLEZA,
                servicio.nombre() + " - realizado por estilistas profesionales",
                direccion.idDireccion(),
                null,
                null
            );

            if (!especificacionService.yaExiste(especificacionDTO)) {
                especificacionService.crearSinId(especificacionDTO);
                System.out.println("✔ Especificación para '" + servicio.nombre() + "' creada.");
            } else {
                System.out.println("✔ Especificación para '" + servicio.nombre() + "' ya existe.");
            }
        }
    }

    private void initDisponibilidades() {
        if (serviciosCreados.size() < 3) {
            System.out.println("No se crearon todos los servicios necesarios para crear disponibilidades.");
            return;
        }

        ServicioDTO servicioA = serviciosCreados.get(0); // Corte Básico
        ServicioDTO servicioB = serviciosCreados.get(1); // Peinado Formal
        ServicioDTO servicioC = serviciosCreados.get(2); // Tintura Express

        crearDisponibilidadSiNoExiste(DiaSemana.LUNES, LocalTime.of(9, 0), LocalTime.of(12, 0), Set.of(servicioA.id()));

        crearDisponibilidadSiNoExiste(DiaSemana.MIERCOLES, LocalTime.of(14, 0), LocalTime.of(18, 0),
                Set.of(servicioA.id(), servicioB.id(), servicioC.id()));
    }

    private void crearDisponibilidadSiNoExiste(DiaSemana dia, LocalTime inicio, LocalTime fin, Set<Long> servicioIds) {
        boolean yaExiste = disponibilidadService.listarTodosConServicios().stream().anyMatch(d ->
            d.diaSemana() == dia &&
            d.horaInicio().equals(inicio) &&
            d.horaFin().equals(fin) &&
            d.servicioIds().containsAll(servicioIds)
        );

        if (!yaExiste) {
            DisponibilidadDTO disponibilidad = new DisponibilidadDTO(null, dia, inicio, fin, servicioIds);
            disponibilidadService.crear(disponibilidad);
            System.out.println("✔ Disponibilidad " + dia + " " + inicio + "-" + fin + " creada.");
        } else {
            System.out.println("✔ Disponibilidad " + dia + " " + inicio + "-" + fin + " ya existe.");
        }
    }
}