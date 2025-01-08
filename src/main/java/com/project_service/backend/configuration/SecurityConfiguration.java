package com.project_service.backend.configuration;

import com.project_service.backend.filter.SecurityAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityAuthFilter securityAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(configurer -> configurer
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(securityAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((configurer) -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionFixation().migrateSession());

        return httpSecurity.build();
    }
}
