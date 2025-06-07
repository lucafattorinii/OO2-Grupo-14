package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "turno")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTurno estado;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "cliente_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "turno_cliente_fk")
    )
    private Cliente cliente;

  
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "disponibilidad_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "turno_disponibilidad_fk")
    )
    private Disponibilidad disponibilidad;

  
    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;


    
    public Turno() { }

    public Turno(LocalDate fecha, LocalTime hora, EstadoTurno estado,
                 Cliente cliente, Disponibilidad disponibilidad, Servicio servicio) {
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.setCliente(cliente);
        this.setDisponibilidad(disponibilidad);
        this.setServicio(servicio);
    }

 // getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Sincroniza relación bidireccional con Cliente.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null && !cliente.getTurnos().contains(this)) {
            cliente.getTurnos().add(this);
        }
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    /**
     * Sincroniza relación bidireccional con Disponibilidad.
     */
    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
        if (disponibilidad != null) {
            if (!disponibilidad.getServicio().equals(this.servicio)) {
                // No es estrictamente bidireccional en Disponibilidad, porque Disponibilidad
                // solo referencia a Servicio. Aquí no agregamos Turno a Disponibilidad.
            }
        }
    }

    public Servicio getServicio() {
        return servicio;
    }

    /**
     * Sincroniza relación bidireccional con Servicio.
     */
 
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }


}
