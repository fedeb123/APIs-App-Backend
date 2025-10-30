package com.uade.tpo.petshop.controllers.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Mapea /uploads/** directamente al directorio local "uploads" (ruta absoluta) para
 * evitar el problema de buscar "uploads/uploads/..." cuando se usa la propiedad
 * spring.web.resources.static-locations con "file:uploads/" y se accede via /uploads/...
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta a la carpeta uploads
        String absolute = Paths.get("uploads").toAbsolutePath().toUri().toString();
        // Log útil (quitar en prod si se desea)
        System.out.println("[STATIC-HANDLER] Sirviendo /uploads/** desde: " + absolute);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolute)
                // Durante debug sin cache; en prod puedes poner, p.ej., 3600
                .setCachePeriod(0);
    }
}
