package com.example.booking.config;

import com.example.booking.DTO.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.management.remote.JMXAuthenticator;
import java.io.IOException;

class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final SecurityContextRepository securityContextRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/auth/login", "POST"));
        setAuthenticationManager(authenticationManager);
        this.securityContextRepository = new HttpSessionSecurityContextRepository();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        LoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
        Authentication authentication = getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        SecurityContextHolder.clearContext();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        SecurityContextHolder.clearContext();
    }
}
