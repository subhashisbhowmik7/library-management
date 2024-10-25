package com.crezam.librarymanagement.config;

import com.crezam.librarymanagement.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                        .requestMatchers("/api/v1/auth/**").permitAll()  // Allow public access to auth endpoints (e.g., login, signup)
                        .requestMatchers(HttpMethod.GET,"/api/v1/members").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/members").hasRole("ADMIN")  
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/members/**").hasRole("ADMIN")  
                        .requestMatchers(HttpMethod.PUT,"/api/v1/members/**").hasRole("ADMIN")  
                        .requestMatchers(HttpMethod.GET,"/api/v1/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/books/**").hasRole("ADMIN")   
                        .anyRequest().authenticated() 
                )
                .authenticationProvider(authenticationProvider())
                
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

     
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
