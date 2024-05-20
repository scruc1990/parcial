package com.dbconnect.dbconnect.Models.Entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    @NotEmpty(message = "El campo no puede estar en vacio")
    @Column(name = "Nombre")
    private String Nombre;

    @NotEmpty(message = "El campo no puede estar en vacio")
    @Column(name = "Apellido")
    private String Apellido;

    @NotEmpty(message = "El campo no puede estar en vacio")
    @Email(message = "Ingrese un correo valido")
    @Column(name = "Email")
    private String Email;

    @Column(name = "create_at")
    @DateTimeFormat(pattern =  "yyyy-mm-dd")
    @Temporal(TemporalType.DATE)
    private Date CreateAt;

    @Column(name = "Password")
    @NotEmpty(message = "El campo no puede estar en vacio")
    private String Password;

    @Column(name = "Role")
    @NotEmpty(message = "El campo no puede estar en vacio")
    private String Role;

    @Column(name = "Enabled")
    @NotEmpty(message = "El campo no puede estar en vacio")
    private Boolean Enabled;

    @PrePersist
    public void PrePersist(){
        CreateAt = new Date();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Date getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(Date createAt) {
        CreateAt = createAt;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public Boolean getEnabled() {
        return Enabled;
    }

    public void setEnabled(Boolean enabled) {
        Enabled = enabled;
    }

    private static final long serialVersionUID = 1L;
}
