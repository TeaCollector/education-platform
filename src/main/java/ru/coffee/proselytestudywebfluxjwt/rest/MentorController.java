package ru.coffee.proselytestudywebfluxjwt.rest;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("api/v1/mentor")
//@PreAuthorize("hasRole('ROLE_MENTOR')")
public class MentorController {

    @GetMapping("lesson")
    public Flux<List<String>> getTask() {
        return Flux.just(List.of("Такой то курс", "И такой..."));
    }
}
