package com.dbconnect.dbconnect.Models.DAO;

import java.util.Optional;

import com.dbconnect.dbconnect.Models.Entity.Usuario;

public interface IUsuarioDao {
    public void saveUser(Usuario usuario);
    Optional<Usuario> findById(Long id);
}
