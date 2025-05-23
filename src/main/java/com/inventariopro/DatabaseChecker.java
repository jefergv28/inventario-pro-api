package com.inventariopro;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseChecker implements CommandLineRunner {

    public DatabaseChecker(DataSource dataSource) {
    }

    @Override
    public void run(String... args) {
        try {
            System.out.println("✅ Conexión a la base de datos exitosa!");
        } catch (Exception e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}
