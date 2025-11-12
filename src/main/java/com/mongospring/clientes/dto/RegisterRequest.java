package com.mongospring.clientes.dto;

import lombok.Data;

@Data
public class RegisterRequest {

	private String correo;
	private String password;
	private String nombre;
}
