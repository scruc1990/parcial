package com.dbconnect.dbconnect.Models.DAO;

import java.util.List;

import com.dbconnect.dbconnect.Models.Entity.Cliente;

public interface IClienteDao {

    public List<Cliente> findAll();

    public void saveClient(Cliente cliente);

    public Cliente findById(Long id);

    public void delete(Long idLong);

    public void edit(Cliente cliente);
    
}
