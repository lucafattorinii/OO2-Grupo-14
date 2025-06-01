package com.grupo14.turnos.modelo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Empleado extends Persona {

    @Column(name = "cuit")
    private Long cuit;

    @Column(name = "legajo")
    private Integer legajo;

    @Column(name = "puesto_cargo", length = 100)
    private String puestoCargo;

   
    @OneToMany(
        mappedBy = "empleado",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<Disponibilidad> disponibilidades = new HashSet<>();

    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "servicio_empleado",
        joinColumns = @JoinColumn(
            name = "empleado_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "servicio_empleado_empleado_fk")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "servicio_id",
            referencedColumnName = "id_servicio",
            foreignKey = @ForeignKey(name = "servicio_empleado_servicio_fk")
        )
    )
    private Set<Servicio> servicios = new HashSet<>();

   
    public Empleado() { }

    public Empleado(String email, String contrasena,
                    String nombre, String apellido, Long dni,
                    Long cuit, Integer legajo, String puestoCargo) {
        super(email, contrasena, nombre, apellido, dni);
        this.cuit = cuit;
        this.legajo = legajo;
        this.puestoCargo = puestoCargo;
    }

   
    // getters y setters

    public Long getCuit() {
        return cuit;
    }

    public void setCuit(Long cuit) {
        this.cuit = cuit;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }

    public String getPuestoCargo() {
        return puestoCargo;
    }

    public void setPuestoCargo(String puestoCargo) {
        this.puestoCargo = puestoCargo;
    }

    public Set<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(Set<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    public Set<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(Set<Servicio> servicios) {
        this.servicios = servicios;
    }

 
    public void addDisponibilidad(Disponibilidad disp) {
        disponibilidades.add(disp);
        disp.setEmpleado(this);
    }

    public void removeDisponibilidad(Disponibilidad disp) {
        disponibilidades.remove(disp);
        disp.setEmpleado(null);
    }

    public void addServicio(Servicio servicio) {
        servicios.add(servicio);
        servicio.getEmpleados().add(this);
    }

    public void removeServicio(Servicio servicio) {
        servicios.remove(servicio);
        servicio.getEmpleados().remove(this);
    }
}
