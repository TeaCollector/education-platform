package ru.coffee.proselytestudywebfluxjwt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;
import ru.coffee.proselytestudywebfluxjwt.security.AuthenticationManager;
import ru.coffee.proselytestudywebfluxjwt.security.BearerTokenAuthenticationConverter;
import ru.coffee.proselytestudywebfluxjwt.security.JwtHandler;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Configuration
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};
    private final String[] routesForStudent = {"/api/v1/student/**"};
    private final String[] routesForMentor = {"/api/v1/mentor/**"};

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthenticationManager authenticationManager) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> {
                    exchange.pathMatchers(HttpMethod.OPTIONS)
                            .permitAll()
                            .pathMatchers(publicRoutes)
                            .permitAll()
                            .pathMatchers(routesForStudent).hasRole("STUDENT")
                            .pathMatchers(routesForMentor).hasRole("MENTOR")
                            .anyExchange()
                            .denyAll();

                })

                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint((swe, e) -> {
                                log.error("In Security web filter chain - unauthorized error: {}", e.getMessage());
                                return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                            })
                            .accessDeniedHandler((swe, e) -> {
                                log.error("In Security web filter chain - accsess denied: {}", e.getMessage());
                                return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                            });
                })
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }



    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);

        bearerAuthenticationFilter.setServerAuthenticationConverter(
                new BearerTokenAuthenticationConverter(new JwtHandler(secret)));

        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }
}
