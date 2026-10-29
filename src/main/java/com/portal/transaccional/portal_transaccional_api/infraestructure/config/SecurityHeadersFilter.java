package com.portal.transaccional.portal_transaccional_api.infraestructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Configuration
public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Protección contra XSS y control de fuentes de contenido (CSP)
        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'; object-src 'none';");

        // 2. Previene ataques de Clickjacking (evita que tu app sea embebida en un iframe malicioso)
        response.setHeader("X-Frame-Options", "DENY");

        // 3. Previene ataques de MIME-sniffing (fuerza al navegador a respetar el Content-Type declarado)
        response.setHeader("X-Content-Type-Options", "nosniff");

        // 4. Protección contra XSS clásica en navegadores antiguos (X-XSS-Protection)
        response.setHeader("X-XSS-Protection", "1; mode=block");

        // 5. Control de la política de referencias (Strict-Transport-Security si estás en HTTPS)
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // Continúa con la ejecución normal de la petición hacia el controlador
        filterChain.doFilter(request, response);
    }
}