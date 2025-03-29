package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.models.StockModel;
import com.inventariopro.crud.repositories.StockRepository;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<StockModel> obtenerStocks() {
        return stockRepository.findAll();
    }

    public StockModel guardarStock(StockModel stock) {
        return stockRepository.save(stock);
    }

    public Optional<StockModel> obtenerPorId(Long id) {
        return stockRepository.findById(id);
    }

    public boolean eliminarStock(Long id) {
        try {
            stockRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
