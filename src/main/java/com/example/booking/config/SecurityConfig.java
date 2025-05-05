package com.example.booking.config;

import com.example.booking.repository.UserRepository;
import com.example.booking.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;
    private final UserRepository userRepository;

    public SecurityConfig(AuthenticationConfiguration authConfiguration, UserRepository userRepository) {
        this.authConfiguration = authConfiguration;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .expiredUrl("/login")
                )
                .securityContext((securityContext) -> securityContext
                        .securityContextRepository(securityContextRepository())
                )
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(authenticationManager());
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error=true"));
        filter.setSecurityContextRepository(securityContextRepository());
        return filter;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
