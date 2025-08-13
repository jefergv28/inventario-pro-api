package com.inventariopro.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // <-- Importa esta clase

@SpringBootApplication
@ComponentScan(basePackages = "com.inventariopro.crud")
@EnableJpaAuditing // <-- Agrega esta anotación
public class CrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }
}