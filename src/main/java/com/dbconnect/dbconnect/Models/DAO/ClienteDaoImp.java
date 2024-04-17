package com.dbconnect.dbconnect.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbconnect.dbconnect.Models.Entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ClienteDaoImp implements IClienteDao {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Cliente> findAll() {

        return em.createQuery("from Cliente").getResultList();
    }

    @Override
    @Transactional
    public void saveClient(Cliente cliente) {
        em.persist(cliente);
    }

    @Override
    @Transactional
    public void delete(Long idLong) {
        Cliente cliente = em.find(Cliente.class, idLong);
        em.remove(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id){
        return em.find(Cliente.class, id);
    }

    @Override
    @Transactional
    public void edit(Cliente cliente) {
        em.merge(cliente);
    }

}
