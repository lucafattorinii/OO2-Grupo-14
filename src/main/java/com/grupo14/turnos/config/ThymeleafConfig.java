// Borra (o renombra) tu ThymeleafEmailConfig.java y en su lugar crea:

package com.grupo14.turnos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {

    /**
     * Resolver para vistas web (error.html, index.html, /view/*).
     */
    @Bean
    public ClassLoaderTemplateResolver webTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");          // classpath:/templates/
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);                     // primero las vistas web
        resolver.setCheckExistence(true);
        return resolver;
    }

    /**
     * Resolver para plantillas de email (confirmacion-turno.html…).
     */
    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/email/");   // classpath:/templates/email/
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(2);                     // después del resolver web
        resolver.setCheckExistence(true);
        return resolver;
    }

    /**
     * Motor que combina ambos resolvers.
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addTemplateResolver(webTemplateResolver());
        engine.addTemplateResolver(emailTemplateResolver());
        return engine;
    }
}
