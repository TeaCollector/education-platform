package ru.coffee.proselytestudywebfluxjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
//@EnableWebSecurity(debug = true)
//@EnableReactiveMethodSecurity
public class ProselyteStudyWebfluxJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProselyteStudyWebfluxJwtApplication.class, args);
    }

}
