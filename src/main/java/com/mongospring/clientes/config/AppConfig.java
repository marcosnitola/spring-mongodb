package com.mongospring.clientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mongospring.clientes.modelo.Usuario;
import com.mongospring.clientes.repositorio.UsuarioRepositorio;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	private final UsuarioRepositorio repositorio;

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService(){
		return username -> {
			Usuario usuario = repositorio.findByCorreo(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			return User.builder()
				.username(usuario.getCorreo())
				.password(usuario.getPassword())
				.build();
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider(){
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
