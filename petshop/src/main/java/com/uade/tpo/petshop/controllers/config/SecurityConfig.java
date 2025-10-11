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
                config.setExposedHeaders(java.util.List.of("Authorization", "Content-Disposition")); // opcional
                config.setAllowCredentials(false); // no se envian cookies

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
                                                //CORS
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                
                                                // Registro, Error y Docs publicos.
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()
                                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                                                //Acciones Administrativas (@ADMIN)
                                                .requestMatchers("/api/usuarios/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers("/api/roles/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers("/api/facturas").hasAnyAuthority(RolEnum.ADMIN.name())

                                                //PRIMER ACCESO
                                                //.requestMatchers("/api/roles/**").permitAll()

                                                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())                                                
                                                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())

                                                // Rutas de pedidos (orden: mas especificos primero hacia mas generales para que no se pisen)
                                                // Cliente ve su pedido por email
                                                .requestMatchers(HttpMethod.GET, "/api/pedidos/usuario").hasAnyAuthority(RolEnum.CLIENTE.name())

                                                .requestMatchers(HttpMethod.GET, "/api/pedidos/*").hasAnyAuthority(RolEnum.ADMIN.name())

                                                .requestMatchers(HttpMethod.GET, "/api/pedidos").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.POST, "/api/pedidos/agregarFactura/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/*/agregarProducto").hasAnyAuthority(RolEnum.CLIENTE.name())
                                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").hasAnyAuthority(RolEnum.ADMIN.name())

                                                //Acciones de cliente
                                                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasAnyAuthority(RolEnum.CLIENTE.name())
                                                .requestMatchers(HttpMethod.DELETE, "/api/detalle-pedidos").hasAnyAuthority(RolEnum.CLIENTE.name())
                                                .requestMatchers(HttpMethod.POST, "/api/detalle-pedidos").hasAnyAuthority(RolEnum.CLIENTE.name())
                                                .requestMatchers(HttpMethod.PUT, "/api/detalle-pedidos").hasAnyAuthority(RolEnum.CLIENTE.name())
                                                
                                                //Acciones tanto para Admin como para Cliente
                                                // @CrossOrigin(localhost del front 5173)
                                                .requestMatchers(HttpMethod.GET, "/api/productos/**").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/categorias/**").authenticated()                                               
                                                .requestMatchers(HttpMethod.GET, "/api/detalle-pedidos").authenticated()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
