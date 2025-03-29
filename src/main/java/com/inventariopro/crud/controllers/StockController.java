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

import com.inventariopro.crud.models.StockModel;
import com.inventariopro.crud.services.StockService;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public List<StockModel> obtenerStocks() {
        return stockService.obtenerStocks();
    }

    @PostMapping
    public StockModel guardarStock(@RequestBody StockModel stock) {
        return stockService.guardarStock(stock);
    }

    @GetMapping("/{id}")
    public Optional<StockModel> obtenerStockPorId(@PathVariable Long id) {
        return stockService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarStock(@PathVariable Long id) {
        boolean eliminado = stockService.eliminarStock(id);
        return eliminado ? "Stock eliminado" : "Error al eliminar el stock";
    }
}
