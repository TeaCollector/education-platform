package ru.coffee.proselytestudywebfluxjwt.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrinciple implements Principal {
    private Long id;
    private String name;
}
