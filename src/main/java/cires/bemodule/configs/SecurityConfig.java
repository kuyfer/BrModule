package cires.bemodule.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

           return http
                    .csrf(customizer -> customizer.disable())// disable csrf
                    .authorizeHttpRequests(request -> request.anyRequest().authenticated()) // authenticate all requests
                    .formLogin(Customizer.withDefaults())// enable form login
                    .httpBasic(Customizer.withDefaults()) // enable basic auth( for rest)
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // making session stateless
                    .build();




        }

}
