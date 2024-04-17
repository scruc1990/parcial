package com.dbconnect.dbconnect.Models.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbconnect.dbconnect.Models.Entity.Encabezado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class EncabezadoDaoImp implements IEncabezadoDao{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Encabezado saveEncabezado(Encabezado encabezado) {
        em.persist(encabezado);
        return encabezado;
    }

    @Override
    @Transactional(readOnly = true)
    public Encabezado findById(Long id) {
        return em.find(Encabezado.class, id);
    }


}
