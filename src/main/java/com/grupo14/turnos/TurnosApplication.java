package com.grupo14.turnos;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TurnosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurnosApplication.class, args);

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
            } else {
                System.out.println("Abrí manualmente: http://localhost:8080/");
            }
        } catch (Exception e) {
            System.out.println("No se pudo abrir el navegador: " + e.getMessage());
        }

    }
}