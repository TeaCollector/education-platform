package ru.coffee.proselytestudywebfluxjwt.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TokenDetails {
    private Long userId;
    private String token;
    private Date issueAt;
    private Date expiresAt;
}
