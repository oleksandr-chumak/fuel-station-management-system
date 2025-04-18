package com.fuelstation.managmentapi.authentication.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.services.JwtTokenFilter;
import com.fuelstation.managmentapi.authentication.infrastructure.services.UserDetailsServiceImp;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/api/auth/login/**").permitAll()
                                .requestMatchers(
                                    "/api/fuel-stations/{id}/deactivate",
                                    "/api/fuel-stations/{id}/assign-manager",
                                    "/api/fuel-stations/{id}/unassign-manager",
                                    "/api/fuel-stations/{id}/change-fuel-price",
                                    "/api/fuel-stations/{id}/unassign-manager",

                                    "/api/managers/{id}/terminate",
                                    "/api/managers/",
                                    "/api/managers/{id}",

                                    "/api/fuel-orders/{id}/confirm",
                                    "/api/fuel-orders/{id}/reject",
                                    "/api/fuel-orders/",
                                    "/api/fuel-orders/{id}"
                                ).hasAuthority(UserRole.Administrator.name())
                                .requestMatchers( 
                                    "/api/fuel-stations/{id}", 
                                    "/api/fuel-stations/",
                                    "/api/fuel-stations/{id}/managers",
                                    "/api/fuel-stations/{id}/fuel-orders"
                                ).hasAnyAuthority(UserRole.Administrator.name(), UserRole.Manager.name())
                                .anyRequest().authenticated() 
                ).userDetailsService(userDetailsServiceImp)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e -> e.accessDeniedHandler(
                                        (request, response, accessDeniedException) -> response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}