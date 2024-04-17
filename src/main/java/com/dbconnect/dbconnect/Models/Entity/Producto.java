package com.dbconnect.dbconnect.Models.Entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;

@Entity
public class Producto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    @NotEmpty(message = "El campo no puede estar en vacio")
    @Column(name = "Nombre")
    private String Nombre;

    @NotEmpty(message = "El campo no puede estar en vacio")
    @Column(name = "Descripcion")
    private String Descripcion;

    @NotNull(message = "Ingrese un valor para el producto")
    @Min(value = 100, message = "Ingrese un valor mayor a 100")
    @Column(name = "ValorUni")
    private Long ValorUni;

    @NotNull(message = "Ingrese un stock para el producto")
    @Min(value = 1, message = "El Stock debe ser mayor a 1")
    @Column(name = "Stock")
    private Long Stock;

    public Producto(Long id, String nombre, String descripcion, Long valorUni, Long stock) {
        super();
        this.Id = id;
        this.Nombre = nombre;
        this.Descripcion = descripcion;
        this.ValorUni = valorUni;
        this.Stock = stock;
    }

    public Producto() {
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

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Long getValorUni() {
        return ValorUni;
    }

    public void setValorUni(Long valorUni) {
        ValorUni = valorUni;
    }

    public Long getStock() {
        return Stock;
    }

    public void setStock(Long stock) {
        Stock = stock;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

}
