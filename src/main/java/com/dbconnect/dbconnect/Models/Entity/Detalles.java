package com.dbconnect.dbconnect.Models.Entity;
import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.Column;

@Entity
public class Detalles implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;
    //@NotEmpty
    @Column(name = "id_encabezado")
    private long idEncabezado;
   // @NotEmpty
    @Column(name = "id_producto")
    private long idProducto;
   // @NotEmpty
    @Column(name = "cantidad")
    private long cantidad;
    //@NotEmpty
    @Column(name = "valor")
    private double valor;
    @Column(name = "descuento")
    private double descuento;
    
    private Producto producto;

    public Detalles() { }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getIdEncabezado() {
        return idEncabezado;
    }
    public void setIdEncabezado(long idEncabezado) {
        this.idEncabezado = idEncabezado;
    }
    public long getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }
    public long getCantidad() {
        return cantidad;
    }
    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public double getDescuento() {
        return descuento;
    }
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    private static final long serialVersionUID = 1L;
}
