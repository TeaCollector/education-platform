package ru.coffee.proselytestudywebfluxjwt.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.coffee.proselytestudywebfluxjwt.entity.UserEntity;
import ru.coffee.proselytestudywebfluxjwt.exception.UnauthorizedException;
import ru.coffee.proselytestudywebfluxjwt.service.UserService;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrinciple customPrinciple = (CustomPrinciple) authentication.getPrincipal();
        return userService.getUserById(customPrinciple.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new UnauthorizedException("User disabled")))
                .map(user -> authentication);
    }
}
