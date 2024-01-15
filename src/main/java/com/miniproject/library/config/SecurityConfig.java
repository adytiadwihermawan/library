package com.miniproject.library.config;

import com.miniproject.library.security.JwtTokenFilter;
import com.miniproject.library.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    private static final String LIBRARIAN = "LIBRARIAN";
    private static final String VISITOR = "VISITOR";

    @Bean
    public AuthenticationManager authManager() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] swagger = {
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**"
        };

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/login", "/users/register/{role}", "/users/all").permitAll()
                        .requestMatchers(swagger).permitAll()
                        .requestMatchers(HttpMethod.GET, "/book/{id}", "/penalty/all", "/penalty/{id}", "/category/all", "/book/all").permitAll()
                        .requestMatchers(HttpMethod.POST, "/category", "/book", "/penalty", "/loan/return").hasRole(LIBRARIAN)
                        .requestMatchers(HttpMethod.PUT, "/users/edit-{id}", "/librarian/edit-{id}", "/anggota/edit-{id}", "/category/edit-{id}", "/book/edit/{id}").hasRole(LIBRARIAN)
                        .requestMatchers(HttpMethod.GET, "/users/{id}", "/librarian/{id}", "/librarian/all", "/category/{id}", "/book").hasRole(LIBRARIAN)
                        .requestMatchers(HttpMethod.DELETE, "/users/delete-{id}", "/librarian/delete-{id}", "/anggota/delete-{id}", "/penalty/{id}").hasRole(LIBRARIAN)
                        .requestMatchers(HttpMethod.POST, "/loan/borrow").hasRole(VISITOR)
                        .requestMatchers(HttpMethod.GET, "/anggota/{id}", "/anggota/all", "/loan/anggota/{anggotaId}/loanId").hasAnyRole(VISITOR, LIBRARIAN)
                        .anyRequest().authenticated()
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}