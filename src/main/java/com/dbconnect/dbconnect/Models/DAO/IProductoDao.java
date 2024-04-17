package com.dbconnect.dbconnect.Models.DAO;

import java.util.List;

import com.dbconnect.dbconnect.Models.Entity.Producto;

public interface IProductoDao {
    public List<Producto> findAll();

    public void saveProducto(Producto producto);

    public Producto findById(Long id);

    public void delete(Long id);

    public void edit(Producto producto);

    // public Optional<Producto> getProducto(Long id);

}
