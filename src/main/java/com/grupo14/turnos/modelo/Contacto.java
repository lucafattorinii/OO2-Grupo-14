package com.grupo14.turnos.modelo;

import jakarta.persistence.*;


@Entity
@Table(name = "contacto")
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "telefono", length = 20)
    private String telefono;

  
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "direccion_id",
        referencedColumnName = "id_direccion",
        foreignKey = @ForeignKey(name = "contacto_direccion_fk")
    )
    private Direccion direccion;

   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "usuario_id",
        referencedColumnName = "id",
        unique = true,
        foreignKey = @ForeignKey(name = "contacto_usuario_fk")
    )
    private Usuario usuario;

   
    public Contacto() { }

    public Contacto(String telefono, Direccion direccion, Usuario usuario) {
        this.telefono = telefono;
        this.direccion = direccion;
        this.usuario = usuario;
    }

   
    // Getters / Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
