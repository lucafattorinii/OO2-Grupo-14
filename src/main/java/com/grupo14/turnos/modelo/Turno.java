package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "turno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "fecha_id", nullable = false, foreignKey = @ForeignKey(name = "turno_fecha_fk"))
    private Fecha fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTurno estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "cliente_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "turno_cliente_fk")
    )
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "disponibilidad_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "turno_disponibilidad_fk")
    )
    private Disponibilidad disponibilidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "servicio_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "turno_servicio_fk")
    )
    private Servicio servicio;
    
    
    public Turno(Fecha fecha, LocalTime hora, EstadoTurno estado,
            Cliente cliente, Disponibilidad disponibilidad, Servicio servicio) {
   this.fecha = fecha;
   this.hora = hora;
   this.estado = estado;
   this.setCliente(cliente);
   this.setDisponibilidad(disponibilidad);
   this.setServicio(servicio);
}

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null && !cliente.getTurnos().contains(this)) {
            cliente.getTurnos().add(this);
        }
    }
}
