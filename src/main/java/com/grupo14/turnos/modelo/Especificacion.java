package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especificacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Especificacion {

    @Id
    @Column(name = "id")
    private long id; // Este ID se compartirá con el ID de Servicio

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "direccion_id",
        referencedColumnName = "id_direccion",
        foreignKey = @ForeignKey(name = "especificacion_direccion_fk")
    )
    private Direccion direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rubro", nullable = false)
    private Rubro rubro;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // Compartir el mismo ID con la entidad Servicio
    @JoinColumn(
        name = "id",
        referencedColumnName = "id_servicio", // clave primaria de Servicio
        foreignKey = @ForeignKey(name = "especificacion_servicio_fk")
    )
    private Servicio servicio;

    @Builder
    public Especificacion(Direccion direccion, Rubro rubro, String detalles, Servicio servicio) {
        this.direccion = direccion;
        this.rubro = rubro;
        this.detalles = detalles;
        this.setServicio(servicio);
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
        if (servicio != null) {
            this.id = servicio.getId();
        }
    }
}
