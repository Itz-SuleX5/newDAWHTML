package com.example.auth0springboot.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                // Forzar el host externo sin puerto si viene de Codespaces
                String forwardedHost = request.getHeader("x-forwarded-host");
                String forwardedProto = request.getHeader("x-forwarded-proto");
                if (forwardedHost != null && forwardedProto != null) {
                    // Elimina el puerto si existe
                    String cleanHost = forwardedHost.replace(":8080", "");
                    request.setAttribute("javax.servlet.forward.request_uri", request.getRequestURI());
                    request.setAttribute("javax.servlet.forward.context_path", request.getContextPath());
                    request.setAttribute("javax.servlet.forward.servlet_path", request.getServletPath());
                    request.setAttribute("javax.servlet.forward.query_string", request.getQueryString());
                    // Forzar el host y el esquema
                    response.setHeader("X-Forwarded-Host", cleanHost);
                    response.setHeader("X-Forwarded-Proto", forwardedProto);
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
