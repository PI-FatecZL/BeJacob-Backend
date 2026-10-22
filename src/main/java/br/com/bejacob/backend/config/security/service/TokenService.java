package br.com.bejacob.backend.config.security.service;

import br.com.bejacob.backend.entity.User;
import br.com.bejacob.backend.exception.token.*;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;

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
            throw new ErrorGeneratedTokenException("Erro ao gerar Token JWT!", e);
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
            throw new InvalidTokenIssuerException("Token inválido: Issuer incorreto!");
        } catch (JwtException e) {
                throw new ExpiredOrInvalidJwtException("Token inválido ou expirado!", e);
        }
    }

    private Date expirationDate() {
        return Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));
    }

    private SecretKey getResetPasswordKey(User user) {
        String combinedSecret = secret + user.getPassword();
        return Keys.hmacShaKeyFor(combinedSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generatePasswordResetToken(User user) {
        try {
            return Jwts.builder()
                    .issuer(issuer)
                    .subject(user.getEmail())
                    .claim("purpose", "RESET_PASSWORD")
                    .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                    .signWith(getResetPasswordKey(user))
                    .compact();
        } catch (JwtException e) {
            throw new ErrorGeneratedTokenException("Erro ao gerar token de recuperação de senha", e);
        }
    }

    public String extractEmailFromResetToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new MalformedTokenException("Token inválido");
            }
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payloadJson);
            return node.get("sub").asText();
        } catch (Exception e) {
            throw new MalformedTokenException("Token de recuperação malformado", e);
        }
    }

    public void validatePasswordResetToken(String token, User user) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getResetPasswordKey(user))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (!issuer.equals(claims.getIssuer())) {
                throw new InvalidTokenIssuerException("Issuer inválido");
            }
            if (!"RESET_PASSWORD".equals(claims.get("purpose"))) {
                throw new InvalidTokenPurposeException("Finalidade do token inválida");
            }
        } catch (JwtException e) {
            throw new ExpiredOrInvalidJwtException("Token inválido, expirado ou já utilizado");
        }
    }
}