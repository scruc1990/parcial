package com.dbconnect.dbconnect.Models.DAO;

import com.dbconnect.dbconnect.Models.Entity.Encabezado;

public interface IEncabezadoDao {

    public Encabezado saveEncabezado(Encabezado encabezado);

    public Encabezado findById(Long id);
    
} 
