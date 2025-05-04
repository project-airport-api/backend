package com.adahe.airport.auth.config;

import com.adahe.airport.auth.filter.JwtAuthenticationEntryPoint;
import com.adahe.airport.auth.filter.JwtAuthenticationFilter;
import com.adahe.airport.auth.service.CustomUserDetailsService;
import com.adahe.airport.shared.enums.Roles;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private CustomUserDetailsService userDetailsService;

    @Resource
    private JwtAuthenticationFilter authenticationFilter;

    @Resource
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> corsConfigurationSource())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                // Permit all login requests
                                .requestMatchers("/login").permitAll()
                                // Permit all swagger-related requests
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/**").hasRole(Roles.ADMIN.name().toUpperCase())
                                .anyRequest().authenticated()
                );

        security.authenticationProvider(authenticationProvider());
        security.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
