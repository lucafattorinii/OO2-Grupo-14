package com.grupo14.turnos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("API Gestión de Turnos")
                .version("1.0")
                .description("Documentación de endpoints para gestión de turnos"));
    }
    
    @Bean
    public OpenApiCustomizer sortTagsAlphabetically() {
        return openApi -> {
            List<Tag> tags = openApi.getTags();
            if (tags != null) {
                // Mover el tag de autenticación al principio
                List<Tag> sortedTags = tags.stream()
                    .sorted((t1, t2) -> {
                        if (t1.getName().equalsIgnoreCase("Autenticación")) return -1;
                        if (t2.getName().equalsIgnoreCase("Autenticación")) return 1;
                        return t1.getName().compareTo(t2.getName());
                    })
                    .collect(Collectors.toList());
                openApi.setTags(sortedTags);
            }
        };
    }
}
