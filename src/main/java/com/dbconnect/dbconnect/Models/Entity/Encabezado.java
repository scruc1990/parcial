package com.dbconnect.dbconnect.Models.Entity;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Encabezado implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    //@NotEmpty
    @Column(name = "id_cliente")
    private long idCliente;

    @Column(name = "create_at")
    @DateTimeFormat(pattern =  "yyyy-mm-dd")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    //@NotEmpty
    @Column(name = "sub_total")
    private double subTotal;
   // @NotEmpty
    @Column(name = "total")
    private double total;
    //@NotEmpty
    @Column(name = "descuento_total")
    private double descuentoTotal;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "descuento")
    private int descuento;

    @PrePersist
    public void PrePersist(){
        fecha = new Date();
    }
    public Encabezado() {
    }
    public Encabezado (Long idCliente) {
        this.idCliente = idCliente;
        this.fecha = new Date();
        this.subTotal = 0;
        this.total = 0;
        this.descuentoTotal = 0;
        this.estado = false;
        this.descuento = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public double getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    private static final long serialVersionUID = 1L;
}
