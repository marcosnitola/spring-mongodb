package com.mongospring.clientes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongospring.clientes.dto.LoginRequest;
import com.mongospring.clientes.dto.RegisterRequest;
import com.mongospring.clientes.dto.TokenResponse;
import com.mongospring.clientes.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService service;

	@PostMapping("/register")
	public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request){
		TokenResponse token = service.register(request);
		return ResponseEntity.ok(token);
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request){
		TokenResponse token = service.login(request);
		return ResponseEntity.ok(token);
	}
}
