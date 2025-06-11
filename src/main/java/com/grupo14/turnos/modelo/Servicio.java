package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // Muchos servicios pueden pertenecer a un mismo prestador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestador_id", nullable = false)
    private Prestador prestador;

    // Un servicio puede tener muchos empleados
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Empleado> empleados = new HashSet<>();

    // Muchos servicios pueden tener muchas disponibilidades
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "servicio_disponibilidad",
        joinColumns = @JoinColumn(name = "servicio_id"),
        inverseJoinColumns = @JoinColumn(name = "disponibilidad_id")
    )
    private Set<Disponibilidad> disponibilidades = new HashSet<>();
    

    // Método para vincular correctamente al prestador
    public void setPrestador(Prestador nuevoPrestador) {
        if (this.prestador != null) {
            this.prestador.getServicios().remove(this);
        }
        this.prestador = nuevoPrestador;
        if (nuevoPrestador != null) {
            nuevoPrestador.getServicios().add(this);
        }
    }

    // Métodos de conveniencia para manejar empleados
    public void addEmpleado(Empleado empleado) {
        empleados.add(empleado);
        empleado.setServicio(this);
    }

    public void removeEmpleado(Empleado empleado) {
        empleados.remove(empleado);
        empleado.setServicio(null);
    }

    // Métodos de conveniencia para disponibilidades
    public void addDisponibilidad(Disponibilidad disponibilidad) {
        disponibilidades.add(disponibilidad);
        disponibilidad.getServicios().add(this);
    }

    public void removeDisponibilidad(Disponibilidad disponibilidad) {
        disponibilidades.remove(disponibilidad);
        disponibilidad.getServicios().remove(this);
    }
}
