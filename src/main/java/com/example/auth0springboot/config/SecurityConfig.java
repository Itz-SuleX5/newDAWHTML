package com.example.auth0springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Permitir acceso a recursos estáticos, páginas de error, raíz y login de Auth0
                .requestMatchers("/css/**", "/js/**", "/images/**", "/error", "/", "/oauth2/authorization/auth0").permitAll()
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                // Página de login personalizada (opcional)
                .loginPage("/oauth2/authorization/auth0")
                // Página de éxito después del login
                .defaultSuccessUrl("/home", true)
                // Página de fallo en el login
                .failureUrl("/login?error")
            )
            .logout(logout -> logout
                // URL para hacer logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // Limpiar la autenticación
                .clearAuthentication(true)
                // Invalidar la sesión
                .invalidateHttpSession(true)
                // Eliminar cookies
                .deleteCookies("JSESSIONID")
                // Redirigir después del logout
                .logoutSuccessUrl("/")
            );

        return http.build();
    }
}