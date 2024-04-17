package com.dbconnect.dbconnect.Models.Entity;
import java.util.List;

public class factura {
    private Encabezado encabezado;
    private List<Detalles> detalle;

    public factura() {
    }

    public factura(Encabezado encabezado, List<Detalles> detalle) {
        this.encabezado = encabezado;
        this.detalle = detalle;
    }

    public Encabezado getEncabezado() {
        return encabezado;
    }
    public void setEncabezado(Encabezado encabezado) {
        this.encabezado = encabezado;
    }
    public List<Detalles> getDetalle() {
        return detalle;
    }
    public void setDetalle(List<Detalles> detalle) {
        this.detalle = detalle;
    }
}
