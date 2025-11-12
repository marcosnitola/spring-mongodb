package com.mongospring.clientes.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mongospring.clientes.modelo.Usuario;
import com.mongospring.clientes.repositorio.UsuarioRepositorio;
import com.mongospring.clientes.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final UsuarioRepositorio usuarioRepositorio;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		if (request.getServletPath().contains("/auth")){
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwtToken = authHeader.substring(7);
		String userEmail = jwtService.extractUsername(jwtToken);

		if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
			return;
		}
		
		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
		final Optional<Usuario> usuario = usuarioRepositorio.findByCorreo(userDetails.getUsername());

		if (usuario.isEmpty()) {
			filterChain.doFilter(request, response);
			return;	
		}
		
		final boolean isTokenValid = jwtService.isTokenValid(jwtToken, usuario.get());
		if (!isTokenValid) {
			return;
		}

		final var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}
