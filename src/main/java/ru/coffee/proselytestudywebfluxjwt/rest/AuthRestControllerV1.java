package ru.coffee.proselytestudywebfluxjwt.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.coffee.proselytestudywebfluxjwt.dto.AuthRequestDto;
import ru.coffee.proselytestudywebfluxjwt.dto.AuthResponseDto;
import ru.coffee.proselytestudywebfluxjwt.dto.UserDto;
import ru.coffee.proselytestudywebfluxjwt.entity.UserEntity;
import ru.coffee.proselytestudywebfluxjwt.mapper.UserMapper;
import ru.coffee.proselytestudywebfluxjwt.security.CustomPrinciple;
import ru.coffee.proselytestudywebfluxjwt.security.SecurityService;
import ru.coffee.proselytestudywebfluxjwt.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserService service;
    private final UserMapper mapper;


    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto) {
        UserEntity entity = mapper.toEntity(userDto);
        UserDto newUserDto = mapper.toDto(entity);
        log.info("USER DTO: {}", newUserDto);
        return service.registerUser(entity)
                .map(mapper::toDto);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
//        var principle = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        log.info("PRINCIPLE: {}", principle);
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssueAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> info(Authentication authentication) {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authenticationIn = context.getAuthentication();
//        String username = authenticationIn.getName();
//        Object principal = authenticationIn.getPrincipal();
//        Collection<? extends GrantedAuthority> authorities = authenticationIn.getAuthorities();
//
//        log.info("USERNAME IN METHOD: {} AND PRINCIPLE: {}", username, principal);


        CustomPrinciple customPrinciple = (CustomPrinciple) authentication.getPrincipal();
        return service
                .getUserById(customPrinciple.getId())
                .map(mapper::toDto);
    }
}
