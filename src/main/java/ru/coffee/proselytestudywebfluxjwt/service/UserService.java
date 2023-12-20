package ru.coffee.proselytestudywebfluxjwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.coffee.proselytestudywebfluxjwt.entity.UserEntity;
import ru.coffee.proselytestudywebfluxjwt.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public Mono<UserEntity> registerUser(UserEntity user) {
        return repository.save(
                user.toBuilder()
                        .password(encoder.encode(user.getPassword()))
                        .role(user.getRole())
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerUser - user: {} created", u);
        });
    }

    public Mono<UserEntity> getUserById(Long id) {
        return repository.findById(id);
    }

    public Mono<UserEntity> getUserByUserName(String username) {
        return repository.findByUsername(username);
    }
}
