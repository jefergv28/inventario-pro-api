package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.StockDTO;
import com.inventariopro.crud.models.StockModel;
import com.inventariopro.crud.services.StockService;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    // Devuelve la lista de stocks como DTOs (con nombreProducto incluido)
    @GetMapping
    public List<StockDTO> obtenerStocks() {
        return stockService.obtenerStocksDTO();
    }

    // Guarda o actualiza un stock (recibe entidad completa)
    @PostMapping
    public StockModel guardarStock(@RequestBody StockModel stock) {
        return stockService.guardarStock(stock);
    }

    // Obtener stock por id como DTO opcional
    @GetMapping("/{id}")
    public Optional<StockDTO> obtenerStockPorId(@PathVariable Long id) {
        return stockService.obtenerPorId(id).map(StockDTO::new);
    }

    // Eliminar stock por id
    @DeleteMapping("/{id}")
    public String eliminarStock(@PathVariable Long id) {
        boolean eliminado = stockService.eliminarStock(id);
        return eliminado ? "Stock eliminado" : "Error al eliminar el stock";
    }
}
