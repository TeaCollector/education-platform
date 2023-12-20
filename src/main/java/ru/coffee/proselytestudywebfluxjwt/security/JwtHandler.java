package ru.coffee.proselytestudywebfluxjwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ru.coffee.proselytestudywebfluxjwt.exception.AuthException;
import ru.coffee.proselytestudywebfluxjwt.exception.UnauthorizedException;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


// Здесь будет проходить валидация токена

@Slf4j
public class JwtHandler {

    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify(String token) {
        Claims claims = getClaimsFromToken(token);
        log.info("CLAIMS ISSUER: {} AND NAME: {}", claims.getIssuer(), claims.get("username"));
        final Date expirationDate = claims.getExpiration();

        if(expirationDate.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public static class VerificationResult {

        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }

}
