package com.inventariopro.crud.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stock")
public class StockModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoModel producto;

    @ManyToOne
    @JoinColumn(name = "almacen_id", nullable = false)
    private AlmacenModel almacen;

    @Column(nullable = false)
    private Integer cantidadDisponible;

    @Column(nullable = false)
    private Integer cantidadMinima;

    @Column(nullable = false)
    private Integer cantidadMaxima;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductoModel getProducto() {
        return producto;
    }

    public void setProducto(ProductoModel producto) {
        this.producto = producto;
    }

    public AlmacenModel getAlmacen() {
        return almacen;
    }

    public void setAlmacen(AlmacenModel almacen) {
        this.almacen = almacen;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(Integer cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public Integer getCantidadMaxima() {
        return cantidadMaxima;
    }

    public void setCantidadMaxima(Integer cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }
}
