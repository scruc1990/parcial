package com.dbconnect.dbconnect.Models.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbconnect.dbconnect.Models.Entity.Producto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductoDaoImp implements IProductoDao {

    @PersistenceContext
    private EntityManager pm;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAll() {
        return pm.createQuery("from Producto").getResultList();
    }

    @Override
    @Transactional
    public void saveProducto(Producto producto) {
        pm.persist(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(Long id) {
        return pm.find(Producto.class, id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Producto producto = pm.find(Producto.class, id);
        pm.remove(producto);
    }

    @Override
    @Transactional
    public void edit(Producto producto) {
        pm.merge(producto);
    }

}
