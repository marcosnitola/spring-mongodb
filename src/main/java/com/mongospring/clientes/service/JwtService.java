package com.mongospring.clientes.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mongospring.clientes.modelo.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	public String extractUsername (String token) {
		final Claims jwtToken = Jwts.parser()
			.verifyWith(getSigninKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return jwtToken.getSubject();
	}

	public String generateToken(Usuario usuario){
		return Jwts.builder()
			.id(usuario.getId())
			.claims(Map.of("nombre",usuario.getNombre()))
			.subject(usuario.getCorreo())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + 86400000))
			.signWith(getSigninKey())
			.compact();
	}

	public boolean isTokenValid(String token, Usuario usuario) {
		String username = extractUsername(token);
		return (username.equals(usuario.getCorreo())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token)  {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token){
		final Claims jwtToken = Jwts.parser()
			.verifyWith(getSigninKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return jwtToken.getExpiration();
	}

	private SecretKey getSigninKey(){
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
