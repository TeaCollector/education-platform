package ru.coffee.proselytestudywebfluxjwt.security;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class UserAuthenticationBearer {

     public static Mono<Authentication> create(JwtHandler.VerificationResult result) {

         Claims claims = result.claims;

         String subject = claims.getSubject();
         String role = claims.get("role", String.class);
         String username = claims.get("username", String.class);

         log.info("User's role: {}", role);

         List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

         Long principleId = Long.parseLong(subject);
         CustomPrinciple principle = new CustomPrinciple(principleId, username);

         return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principle, null, authorities));
     }
}
