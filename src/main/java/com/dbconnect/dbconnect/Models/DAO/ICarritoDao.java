package com.dbconnect.dbconnect.Models.DAO;
import com.dbconnect.dbconnect.Models.Entity.factura;
import com.dbconnect.dbconnect.Models.Entity.Carrito;
import com.dbconnect.dbconnect.Models.Entity.Detalles;
import com.dbconnect.dbconnect.Models.Entity.Encabezado;

public interface ICarritoDao {

    // Metodos del carrito
    public factura findAll(long idCliente);

    public void Save(Encabezado head, boolean estado);

    public void add( Carrito carrito);

    public void delete(Long idLong);   

    public void descuento(int descuento, long encabezado);
}
