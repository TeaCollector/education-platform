package ru.coffee.proselytestudywebfluxjwt.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.coffee.proselytestudywebfluxjwt.entity.UserEntity;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {

    Mono<UserEntity> findByUsername(String userName);
}
