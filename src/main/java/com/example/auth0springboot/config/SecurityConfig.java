package com.example.auth0springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .authorizeRequests()
                // Rutas públicas
                .antMatchers("/css/**", "/js/**", "/images/**", "/error", "/", "/oauth2/authorization/auth0").permitAll()
                // Rutas de administrador
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Rutas del dashboard requieren autenticación
                .antMatchers("/dashboard/**").hasRole("USER")
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
                .and()
            .oauth2Login()
                .loginPage("/oauth2/authorization/auth0")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error")
                .userInfoEndpoint()
                    .oidcUserService(this.oidcUserService())
                    .and()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            
            List<GrantedAuthority> authorities = new ArrayList<>();
            
            // Agregar rol básico de usuario
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            
            // Obtener claims
            Map<String, Object> claims = new HashMap<>(oidcUser.getClaims());
            
            // Verificar roles desde Auth0
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getOrDefault("https://tu-app.com/roles", new ArrayList<>());
            
            // Agregar rol de admin si está presente en los claims
            if (roles.contains("admin")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            
            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }
}