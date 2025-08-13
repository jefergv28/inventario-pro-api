package com.inventariopro.crud.dto;

public class ProductoMovidoDTO {
    private String nombreProducto;
    private Long totalMovimientos;

    public ProductoMovidoDTO(String nombreProducto, Long totalMovimientos) {
        this.nombreProducto = nombreProducto;
        this.totalMovimientos = totalMovimientos;
    }

    // Getters y Setters
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Long getTotalMovimientos() {
        return totalMovimientos;
    }

    public void setTotalMovimientos(Long totalMovimientos) {
        this.totalMovimientos = totalMovimientos;
    }
}