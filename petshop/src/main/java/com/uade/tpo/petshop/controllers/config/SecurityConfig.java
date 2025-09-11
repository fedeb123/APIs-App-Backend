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

import com.uade.tpo.petshop.entity.enums.RolEnum;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final FiltroAutenticacionJwt filtroJwt;
        private final AuthenticationProvider authenticationProvider;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req
                                                // Registro, Error y Docs publicos.
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()
                                                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                                                //Acciones Administrativas (@ADMIN)
                                                .requestMatchers("/api/usuarios/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers("/api/roles/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers("/api/facturas").hasAnyAuthority(RolEnum.ADMIN.name())

                                                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAnyAuthority(RolEnum.ADMIN.name())                                                
                                                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.POST, "/api/pedidos/agregarFactura/**").hasAnyAuthority(RolEnum.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/api/detalle-pedidos").authenticated()

                                                //Acciones tanto para Admin como para Cliente
                                                .requestMatchers(HttpMethod.GET, "/api/productos/**").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").authenticated()
                                                .requestMatchers(HttpMethod.GET, "/api/categorias/**").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/detalle-pedidos").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/api/detalle-pedidos").authenticated()                                                
                                                .requestMatchers(HttpMethod.GET, "/api/detalle-pedidos").authenticated()                                                

                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
