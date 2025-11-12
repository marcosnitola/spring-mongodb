package com.mongospring.clientes.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mongospring.clientes.dto.RegisterRequest;
import com.mongospring.clientes.dto.LoginRequest;
import com.mongospring.clientes.dto.TokenResponse;
import com.mongospring.clientes.modelo.Usuario;
import com.mongospring.clientes.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UsuarioRepositorio usuarioRepositorio;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public TokenResponse register(RegisterRequest request){
		Usuario usuario = new Usuario();
		usuario.setNombre(request.getNombre());
		usuario.setCorreo(request.getCorreo());
		usuario.setPassword(passwordEncoder.encode( request.getPassword()));

		Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

		String jwtToken = jwtService.generateToken(usuarioGuardado);

		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(jwtToken);

		return tokenResponse;
	}

	public TokenResponse login(LoginRequest request){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					request.getCorreo(),
					request.getPassword()
				)
		);

		Usuario usuario = usuarioRepositorio.findByCorreo(request.getCorreo()).orElseThrow();
		String jwtToken = jwtService.generateToken(usuario);

		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setToken(jwtToken);

		return tokenResponse;
	}
}
