package com.example.auth0springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir orígenes configurados
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://obscure-waffle-pjrqgrjj4475265xr-3000.app.github.dev");
        
        // Permitir credenciales
        config.setAllowCredentials(true);
        
        // Permitir métodos específicos
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        
        // Permitir headers específicos
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("X-Requested-With");
        
        // Aplicar configuración a todas las rutas
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
