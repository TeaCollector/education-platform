package ru.coffee.proselytestudywebfluxjwt.rest;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/student")
//@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentController {

    @GetMapping("course")
//    @PreAuthorize("hasRole('STUDENT')")
    public Mono<ResponseEntity> testGetRequest() {
        return Mono.just(ResponseEntity.ok().body("COURSE RIGHT!"));
    }

    @GetMapping("lesson")
    public Mono<ResponseEntity> getLesson() {
        return Mono.just(ResponseEntity.ok().body("LESSON RIGHT!"));
    }
}
