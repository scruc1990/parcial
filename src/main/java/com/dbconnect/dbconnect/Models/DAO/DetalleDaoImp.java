package com.dbconnect.dbconnect.Models.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbconnect.dbconnect.Models.Entity.Detalles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class DetalleDaoImp implements IDetalleDao{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveDetalle(Detalles detalles){
        em.persist(detalles);
    }
}
