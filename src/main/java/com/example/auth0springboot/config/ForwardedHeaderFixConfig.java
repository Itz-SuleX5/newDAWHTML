package com.example.auth0springboot.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class ForwardedHeaderFixConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Filter forwardedHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
                    throws jakarta.servlet.ServletException, IOException {
                // Forzar el host externo sin puerto si viene de Codespaces
                String forwardedHost = request.getHeader("x-forwarded-host");
                String forwardedProto = request.getHeader("x-forwarded-proto");
                if (forwardedHost != null && forwardedProto != null) {
                    // Elimina el puerto si existe
                    String cleanHost = forwardedHost.replace(":8080", "");
                    request.setAttribute("jakarta.servlet.forward.request_uri", request.getRequestURI());
                    request.setAttribute("jakarta.servlet.forward.context_path", request.getContextPath());
                    request.setAttribute("jakarta.servlet.forward.servlet_path", request.getServletPath());
                    request.setAttribute("jakarta.servlet.forward.query_string", request.getQueryString());
                    // Forzar el host y el esquema
                    response.setHeader("X-Forwarded-Host", cleanHost);
                    response.setHeader("X-Forwarded-Proto", forwardedProto);
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
