package com.example.auth0springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String adminPanel(Model model) {
        try {
            // Verificar si el usuario actual es admin
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                
                String token = getManagementToken();
                List<Map<String, Object>> users = getUsers(token);
                model.addAttribute("users", users);
                return "admin/panel";
            } else {
                model.addAttribute("error", "No tienes permisos de administrador");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los usuarios: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/users/{id}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            String token = getManagementToken();
            String url = String.format("https://%s/api/v2/users/%s", auth0Domain, id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/block")
    @ResponseBody
    public ResponseEntity<?> blockUser(@PathVariable String id) {
        try {
            String token = getManagementToken();
            String url = String.format("https://%s/api/v2/users/%s", auth0Domain, id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Boolean> body = Map.of("blocked", true);
            HttpEntity<?> entity = new HttpEntity<>(body, headers);
            
            restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/unblock")
    @ResponseBody
    public ResponseEntity<?> unblockUser(@PathVariable String id) {
        try {
            String token = getManagementToken();
            String url = String.format("https://%s/api/v2/users/%s", auth0Domain, id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Boolean> body = Map.of("blocked", false);
            HttpEntity<?> entity = new HttpEntity<>(body, headers);
            
            restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/make-admin")
    @ResponseBody
    public ResponseEntity<?> makeUserAdmin(@PathVariable String id) {
        try {
            String token = getManagementToken();
            String url = String.format("https://%s/api/v2/users/%s", auth0Domain, id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("is_admin", true);
            
            Map<String, Object> body = new HashMap<>();
            body.put("app_metadata", metadata);
            
            HttpEntity<?> entity = new HttpEntity<>(body, headers);
            restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{id}/remove-admin")
    @ResponseBody
    public ResponseEntity<?> removeUserAdmin(@PathVariable String id) {
        try {
            String token = getManagementToken();
            String url = String.format("https://%s/api/v2/users/%s", auth0Domain, id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("is_admin", false);
            
            Map<String, Object> body = new HashMap<>();
            body.put("app_metadata", metadata);
            
            HttpEntity<?> entity = new HttpEntity<>(body, headers);
            restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private String getManagementToken() {
        String url = String.format("https://%s/oauth/token", auth0Domain);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> body = Map.of(
            "grant_type", "client_credentials",
            "client_id", clientId,
            "client_secret", clientSecret,
            "audience", String.format("https://%s/api/v2/", auth0Domain)
        );

        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private List<Map<String, Object>> getUsers(String token) {
        String url = String.format("https://%s/api/v2/users?include_totals=true&per_page=100", auth0Domain);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return (List<Map<String, Object>>) response.getBody().get("users");
    }
}
