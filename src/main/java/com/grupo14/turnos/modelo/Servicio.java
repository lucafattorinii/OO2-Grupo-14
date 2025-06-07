package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "duracion_min")
    private Integer duracionMin;

    @Column(name = "precio")
    private Double precio;

 
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "prestador_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "servicio_prestador_fk")
    )
    private Prestador prestador;

    

 
    @OneToMany(
        mappedBy = "servicio",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<Disponibilidad> disponibilidades = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "servicio_empleado",
        joinColumns = @JoinColumn(
            name = "servicio_id",
            referencedColumnName = "id_servicio",
            foreignKey = @ForeignKey(name = "servicio_empleado_servicio_fk")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "empleado_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "servicio_empleado_empleado_fk")
        )
    )
    private Set<Empleado> empleados = new HashSet<>();

 
    @OneToOne(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Especificacion especificacion;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    
    
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Turno> turnos = new HashSet<>();


    public Servicio() { }

    public Servicio(String nombre, Integer duracionMin, Double precio, Prestador prestador) {
        this.nombre = nombre;
        this.duracionMin = duracionMin;
        this.precio = precio;
        this.setPrestador(prestador);
    }

    // getters y setters
    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(Integer duracionMin) {
        this.duracionMin = duracionMin;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Prestador getPrestador() {
        return prestador;
    }

    
    public void setPrestador(Prestador nuevoPrestador) {
        if (this.prestador != null) {
            this.prestador.getServicios().remove(this);
        }
        this.prestador = nuevoPrestador;
        if (nuevoPrestador != null) {
            nuevoPrestador.getServicios().add(this);
        }
    }

    
    
   

    public Set<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(Set<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    public Set<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(Set<Empleado> empleados) {
        this.empleados = empleados;
    }

    public Set<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(Set<Turno> turnos) {
        this.turnos = turnos;
    }


    public void addDisponibilidad(Disponibilidad disp) {
        disponibilidades.add(disp);
        disp.setServicio(this);
    }

    public void removeDisponibilidad(Disponibilidad disp) {
        disponibilidades.remove(disp);
        disp.setServicio(null);
    }

    public void addEmpleado(Empleado emp) {
        empleados.add(emp);
        emp.getServicios().add(this);
    }

    public void removeEmpleado(Empleado emp) {
        empleados.remove(emp);
        emp.getServicios().remove(this);
    }

    public void addTurno(Turno turno) {
        turnos.add(turno);
        if (turno.getServicio() != this) {
            turno.setServicio(this);
        }
    }


    public void removeTurno(Turno turno) {
        turnos.remove(turno);
        if (turno.getServicio() == this) {
            turno.setServicio(null);
        }
    }

}
