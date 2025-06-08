package com.grupo14.turnos.modelo;

import jakarta.persistence.*;


@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;
    
    @Column(name = "rol", length = 20, nullable = false)
    private String rol;

    
    public Usuario() { }

    public Usuario(String email, String contrasena, String rol) {
        this.email = email;
        this.contrasena = contrasena;
        this.rol=rol;
    }

    
 // getters y setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}
