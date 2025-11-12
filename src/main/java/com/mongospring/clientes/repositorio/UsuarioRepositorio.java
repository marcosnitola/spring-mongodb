package com.mongospring.clientes.repositorio;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongospring.clientes.modelo.Usuario;

public interface UsuarioRepositorio extends MongoRepository<Usuario, String>{

	Optional<Usuario> findByCorreo(String correo);
}
