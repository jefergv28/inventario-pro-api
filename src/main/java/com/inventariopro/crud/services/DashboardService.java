package com.inventariopro.crud.services;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.inventariopro.crud.repositories.CategoriaRepository;
import com.inventariopro.crud.repositories.ProductoRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public Map<String, Object> getDashboardData() {
        // Obtenemos los conteos de la base de datos
        long totalProductos = productoRepository.count();
        long totalCategorias = categoriaRepository.count();
        long stockBajo = productoRepository.countByCantidadProductoLessThan(10); // Ejemplo: Stock bajo si es menor a 10

        // Creamos el mapa con los datos correctos
        Map<String, Object> data = new HashMap<>();
        data.put("totalProductos", totalProductos);
        data.put("totalCategorias", totalCategorias);
        data.put("stockBajo", stockBajo);

        return data;
    }
}