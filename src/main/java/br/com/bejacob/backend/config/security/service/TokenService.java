package br.com.bejacob.backend.config.security.service;

import br.com.bejacob.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    private final String issuer = "BeJacob";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        try {
            return Jwts.builder()
                    .issuer(issuer)
                    .subject(user.getLogin())
                    .expiration(expirationDate())
                    .signWith(getSigningKey())
                    .compact();
        } catch (JwtException e) {
            throw new RuntimeException("Erro ao gerar Token JWT!", e);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(tokenJWT)
                    .getPayload();

            if (issuer.equals(claims.getIssuer())) {
                return claims.getSubject();
            }
            throw new RuntimeException("Token inválido: Issuer incorreto!");
        } catch (JwtException e) {
                throw new RuntimeException("Token inválido ou expirado!", e);
        }
    }

    private Date expirationDate() {
        return Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));
    }
}