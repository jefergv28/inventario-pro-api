package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.dto.StockDTO;
import com.inventariopro.crud.models.StockModel;
import com.inventariopro.crud.repositories.StockRepository;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    // Retorna lista completa de entidades StockModel
    public List<StockModel> obtenerStocks() {
        return stockRepository.findAll();
    }

    // Retorna lista de DTOs para usar en la API y evitar exponer entidades completas
    public List<StockDTO> obtenerStocksDTO() {
        return stockRepository.findAll()
                .stream()
                .map(StockDTO::new) // Constructor que recibe StockModel y transforma a DTO
                .collect(Collectors.toList());
    }

    // Guarda o actualiza un stock
    public StockModel guardarStock(StockModel stock) {
        return stockRepository.save(stock);
    }

    // Busca stock por id
    public Optional<StockModel> obtenerPorId(Long id) {
        return stockRepository.findById(id);
    }

    // Elimina stock por id, retorna true si éxito, false si falla
    public boolean eliminarStock(Long id) {
        try {
            stockRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

public List<StockDTO> obtenerStocksDTOPorUsuario(String email) {
    List<StockModel> stocks = stockRepository.findByUsuario_Email(email);
    return stocks.stream().map(StockDTO::new).toList();
}



}
