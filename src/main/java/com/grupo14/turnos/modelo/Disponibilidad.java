package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "disponibilidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "servicio_disponibilidad",
            joinColumns = @JoinColumn(name = "disponibilidad_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id"),
            uniqueConstraints = @UniqueConstraint(          //  evita pares duplicados
                name = "uk_servicio_disponibilidad",
                columnNames = {"disponibilidad_id", "servicio_id"}
            )
        )
    private Set<Servicio> servicios = new HashSet<>();

    // Constructor sin id (por si querés crear sin pk)
    public Disponibilidad(DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin, Set<Servicio> servicios) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.servicios = servicios != null ? servicios : new HashSet<>();
    }
    
    // Métodos auxiliares para mantener la relación bidireccional si querés
    public void addServicio(Servicio servicio) {
        this.servicios.add(servicio);
        if (!servicio.getDisponibilidades().contains(this)) {
            servicio.getDisponibilidades().add(this);
        }
    }

    public void removeServicio(Servicio servicio) {
        this.servicios.remove(servicio);
        servicio.getDisponibilidades().remove(this);
    }
}
