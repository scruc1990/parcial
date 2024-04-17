package com.dbconnect.dbconnect.Models.Entity;

public class Carrito {
    private long idCliente;
    private long idProducto;
    private int descuento;
    private int cantidad;

    public Carrito() {
    }
    public Carrito(long idCliente) {
        this.idCliente = idCliente;
    }
    public long getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }
    public long getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public int getDescuento() {
        return descuento;
    }
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }
}
