package com.fuelstation.managmentapi.authentication.infrastructure.configuration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fuelstation.managmentapi.authentication.domain.UserRole;
import com.fuelstation.managmentapi.authentication.infrastructure.services.JwtTokenFilter;
import com.fuelstation.managmentapi.authentication.infrastructure.services.UserDetailsServiceImp;

@Configuration
public class SecurityConfig {

    @Value("${client.origin}")
    private String clientOrigin;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                    req -> req
                            .requestMatchers("/api/auth/login/**").permitAll()
                            .requestMatchers(
                                "/api/fuel-stations/",
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
                                "/api/fuel-stations/{id}/managers",
                                "/api/fuel-stations/{id}/fuel-orders",
                                
                                "/api/manager/{id}/fuel-stations"
                            ).hasAnyAuthority(UserRole.Administrator.name(), UserRole.Manager.name())
                            .anyRequest().authenticated() 
            ).userDetailsService(userDetailsServiceImp)
            .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(
                e -> e.accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403))
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        System.out.println("Configuring CORS for origin: " + clientOrigin);
        corsConfiguration.setAllowedOrigins(List.of(clientOrigin));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
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