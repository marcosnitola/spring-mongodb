package com.mongospring.clientes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class Test {
	@GetMapping
	public String holamundo(){
		return "Hola mundo";
	}
}
