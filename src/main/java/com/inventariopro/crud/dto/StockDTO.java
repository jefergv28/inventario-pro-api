package com.inventariopro.crud.dto;

import com.inventariopro.crud.models.StockModel;

public class StockDTO {
    private Long id;
    private int cantidadDisponible;
    private int cantidadMinima;
    private int cantidadMaxima;
    private Long productoId;
    private String productoNombre;

    public StockDTO(Long id, int cantidadDisponible, int cantidadMinima, int cantidadMaxima, Long productoId, String productoNombre) {
        this.id = id;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadMinima = cantidadMinima;
        this.cantidadMaxima = cantidadMaxima;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
    }

    public StockDTO(StockModel stock) {
        this.id = stock.getId();
        this.cantidadDisponible = stock.getCantidadDisponible();
        this.cantidadMinima = stock.getCantidadMinima();
        this.cantidadMaxima = stock.getCantidadMaxima();
        this.productoId = stock.getProducto().getId();
        this.productoNombre = stock.getProducto().getNombreProducto();
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public int getCantidadMinima() { return cantidadMinima; }
    public void setCantidadMinima(int cantidadMinima) { this.cantidadMinima = cantidadMinima; }

    public int getCantidadMaxima() { return cantidadMaxima; }
    public void setCantidadMaxima(int cantidadMaxima) { this.cantidadMaxima = cantidadMaxima; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
}
