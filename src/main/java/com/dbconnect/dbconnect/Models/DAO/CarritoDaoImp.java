package com.dbconnect.dbconnect.Models.DAO;
import com.dbconnect.dbconnect.Models.Entity.factura;
import com.dbconnect.dbconnect.Models.Entity.Producto;
import com.dbconnect.dbconnect.Models.Entity.Carrito;
import com.dbconnect.dbconnect.Models.Entity.Detalles;
import com.dbconnect.dbconnect.Models.Entity.Encabezado;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CarritoDaoImp implements ICarritoDao {

    @PersistenceContext
    private EntityManager pm;

    @Override
    @Transactional(readOnly = true)
    public factura findAll(long idCliente) {
        Encabezado encabezado = pm.createQuery("SELECT e FROM Encabezado e WHERE e.idCliente = :Cliente and e.estado = :Estado", Encabezado.class)
                       .setParameter("Cliente", idCliente)
                       .setParameter("Estado", false)
                       .getSingleResult();
        List<Detalles> detalles = pm.createQuery("SELECT d FROM Detalles d WHERE d.idEncabezado = :encabezadoId", Detalles.class)
                                   .setParameter("encabezadoId", encabezado.getId())
                                   .getResultList();

        List<Producto> productos = pm.createQuery("SELECT p FROM Producto p", Producto.class)
                                   .getResultList();

        for (Detalles detalle : detalles) {
            for (Producto producto : productos) {
                if (detalle.getIdProducto() == producto.getId()) {
                    detalle.setProducto(producto);
                }
            }
        }
        return new factura(encabezado, detalles);
    }

    @Override
    @Transactional
    public void Save(Encabezado head, boolean estado){
        Encabezado encabezado = pm.find(Encabezado.class, head.getId());
        if (encabezado != null) {
            // Flujo para finalizar la compra
            List<Detalles> detalleCompra = pm.createQuery("SELECT d FROM Detalles d WHERE d.idEncabezado = :idEncabezado", Detalles.class)
                        .setParameter("idEncabezado", encabezado.getId())
                        .getResultList();
                        
            for(Detalles detalle : detalleCompra){
                this.updateProducto(detalle.getIdProducto(), detalle.getCantidad());
            }

        }
    }

    @Override
    @Transactional
    public void delete(Long idLong) {
        Detalles detalles = pm.find(Detalles.class, idLong);
        Encabezado encabezado = pm.find(Encabezado.class, detalles.getIdEncabezado());
        pm.remove(detalles);

        this.updateSubtotal(encabezado);
    }

    @Override
    @Transactional
    public void add(Carrito carrito) {
        Encabezado encabezado = new Encabezado(carrito.getIdCliente());
        Detalles detalles = new Detalles();

        Producto producto = pm.find(Producto.class, carrito.getIdProducto());

        encabezado = this.encabezado(encabezado);

        List<Detalles> detalleCompra = pm.createQuery("SELECT d FROM Detalles d WHERE d.idEncabezado = :idEncabezado and d.idProducto = :idProducto", Detalles.class)
                        .setParameter("idEncabezado", encabezado.getId())
                        .setParameter("idProducto", carrito.getIdProducto())
                        .getResultList();

        if (!detalleCompra.isEmpty()) { 
            detalles = detalleCompra.get(0);
            detalles.setCantidad(carrito.getCantidad());
            detalles.setValor(producto.getValorUni() * detalles.getCantidad());
            pm.merge(detalles);
        } else {
            if  (encabezado.getDescuento() > 0) {
                detalles.setDescuento(producto.getValorUni() - (producto.getValorUni() * encabezado.getDescuentoTotal())/100);
            }
            detalles.setIdProducto(carrito.getIdProducto());
            detalles.setCantidad(carrito.getCantidad());
            detalles.setIdEncabezado(encabezado.getId());
            detalles.setValor(producto.getValorUni() * carrito.getCantidad());
            pm.persist(detalles);
        }
        this.updateSubtotal(encabezado);
    }

    @Override
    @Transactional
    public void descuento(int descuento, long idEncabezado) {
        Encabezado encabezado = pm.find(Encabezado.class, idEncabezado);
        encabezado.setDescuento(descuento);

        List<Detalles> detalleCompra = pm.createQuery("SELECT d FROM Detalles d WHERE d.idEncabezado = :idEncabezado", Detalles.class)
                        .setParameter("idEncabezado", encabezado.getId())
                        .getResultList();
                        
            for(Detalles detalle : detalleCompra){
                // Actualizar valor según descuento
                System.out.println("Valor: " + detalle.getValor());
                detalle.setDescuento(((detalle.getValor() * descuento)/100));
                pm.merge(detalle);
            }


        encabezado.setDescuentoTotal((encabezado.getSubTotal() * descuento) / 100);
        encabezado.setTotal(encabezado.getSubTotal() - encabezado.getDescuentoTotal());
        pm.merge(encabezado);

    }

    private Encabezado encabezado (Encabezado head){

        
            List<Encabezado> encabezado = pm.createQuery("SELECT e FROM Encabezado e WHERE e.idCliente = :Cliente and e.estado = false", Encabezado.class)
                                   .setParameter("Cliente", head.getIdCliente())
                                   .getResultList();

        if  (encabezado.isEmpty()) {            
            pm.persist(head);
            encabezado = pm.createQuery("SELECT e FROM Encabezado e WHERE e.idCliente = :Cliente and e.estado = false", Encabezado.class)
                                   .setParameter("Cliente", head.getIdCliente())
                                   .getResultList();
        }

            return encabezado.get(0);
        
    }
    
    private void updateProducto(Long idProducto, long cantidad){
        Producto producto = pm.find(Producto.class, idProducto);
        producto.setStock(producto.getStock() - cantidad);
        pm.merge(producto);
    }

    private void updateSubtotal (Encabezado encabezado) {
        List<Detalles> detalles = pm.createQuery("SELECT d FROM Detalles d WHERE d.idEncabezado = :idEncabezado", Detalles.class)
                                   .setParameter("idEncabezado", encabezado.getId())
                                   .getResultList();
        if (!detalles.isEmpty()) {
        
            double subtotal = 0;
            for(Detalles detalle : detalles){
                // Actualizar valor según descuento
                if (encabezado.getDescuento() > 0) {
                    detalle.setDescuento((detalle.getValor() * encabezado.getDescuentoTotal()));
                    pm.merge(detalle);
                }
                subtotal += detalle.getValor();
            }
            encabezado.setSubTotal(subtotal);

            // Actualizar descuento total
            if (encabezado.getDescuento() > 0) {
                encabezado.setDescuentoTotal((encabezado.getSubTotal() * encabezado.getDescuento()) / 100);
                encabezado.setTotal(encabezado.getSubTotal() - encabezado.getDescuentoTotal());
            }

            pm.merge(encabezado);
        }
    }
}
