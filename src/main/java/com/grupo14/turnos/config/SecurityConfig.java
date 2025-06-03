package com.grupo14.turnos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/login", "/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            //.httpBasic(Customizer.withDefaults());
        	.formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        var admin = User.withUsername("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();
        
        
        var empleado = User.withUsername("empleado")
                .password(passwordEncoder.encode("empleado123"))
                .roles("EMPLEADO")
                .build();

            var cliente = User.withUsername("cliente")
                .password(passwordEncoder.encode("cliente123"))
                .roles("CLIENTE")
                .build();
            
        return new InMemoryUserDetailsManager(admin, empleado, cliente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    

}
