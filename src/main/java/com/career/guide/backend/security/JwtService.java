package com.career.guide.backend.security;

import com.career.guide.backend.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

	private final JwtProperties properties;

	public JwtService(JwtProperties properties) {
		this.properties = properties;
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String subject, Map<String, Object> claims) {
		Instant now = Instant.now();
		Instant expiry = now.plusSeconds(properties.getExpirationSeconds());
		return Jwts.builder()
				.issuer("career-guide")
				.subject(subject)
				.claims(claims)
				.issuedAt(Date.from(now))
				.expiration(Date.from(expiry))
				.signWith(getSigningKey())
				.compact();
	}

	public String extractSubject(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
}


