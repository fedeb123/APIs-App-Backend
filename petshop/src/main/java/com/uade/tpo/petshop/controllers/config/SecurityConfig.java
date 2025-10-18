package com.uade.tpo.petshop.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.uade.tpo.petshop.entity.enums.RolEnum;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FiltroAutenticacionJwt filtroJwt;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        config.setExposedHeaders(java.util.List.of("Authorization", "Content-Disposition"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(req -> req
                // Rutas Públicas
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()

                // Reglas de Admin
                .requestMatchers("/api/roles/**").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers("/api/facturas").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority(RolEnum.ADMIN.name())

                // --- REGLAS DE PEDIDOS ORDENADAS CORRECTAMENTE ---
                // 1. Rutas específicas para CLIENTE
                .requestMatchers(HttpMethod.GET, "/api/pedidos/usuario").hasAnyAuthority(RolEnum.CLIENTE.name())
                .requestMatchers(HttpMethod.PUT, "/api/pedidos/*/agregarProducto").hasAnyAuthority(RolEnum.CLIENTE.name())
                .requestMatchers(HttpMethod.PUT, "/api/pedidos/*/confirmar").hasAnyAuthority(RolEnum.CLIENTE.name(), RolEnum.ADMIN.name()) // Movida aquí arriba y permite ADMIN también
                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasAnyAuthority(RolEnum.CLIENTE.name())
                
                // 2. Rutas generales para ADMIN (después de las específicas)
                .requestMatchers(HttpMethod.GET, "/api/pedidos/*").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/api/pedidos").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/api/pedidos/agregarFactura/**").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").hasAnyAuthority(RolEnum.ADMIN.name()) // Regla general al final

                // Reglas de Detalle Pedido (Cliente)
                .requestMatchers(HttpMethod.GET, "/api/detalle-pedidos").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/detalle-pedidos").hasAnyAuthority(RolEnum.CLIENTE.name())
                .requestMatchers(HttpMethod.POST, "/api/detalle-pedidos").hasAnyAuthority(RolEnum.CLIENTE.name())
                .requestMatchers(HttpMethod.PUT, "/api/detalle-pedidos").hasAnyAuthority(RolEnum.CLIENTE.name())

                // Reglas de Usuarios
                .requestMatchers(HttpMethod.GET, "/api/usuarios/usuario").hasAnyAuthority(RolEnum.CLIENTE.name())
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority(RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyAuthority(RolEnum.CLIENTE.name(), RolEnum.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority(RolEnum.ADMIN.name())
                
                // Denegar todo lo demás si no está autenticado
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
