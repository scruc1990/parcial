package com.dbconnect.dbconnect.Models.DAO;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dbconnect.dbconnect.Models.Entity.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class UsuarioDaoImp implements IUsuarioDao {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveUser(Usuario usuario) {
        em.persist(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return em.findById(id);
    }

    
}
