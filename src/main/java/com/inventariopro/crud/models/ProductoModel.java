package com.inventariopro.crud.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Define que esta clase representa una entidad en la base de datos
@Entity
@Table(name = "productos") // Asocia esta entidad con la tabla "productos"
public class ProductoModel {

    // Clave primaria autoincremental de la tabla productos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna que almacena el nombre del producto
    @Column
    private String nombreProducto;

    // Columna que almacena el precio del producto
    @Column
    private Double precioProducto;

    // Columna que almacena la cantidad de productos disponibles
    @Column
    private Integer cantidadProducto;

    // Métodos getter y setter para acceder y modificar los atributos de la entidad

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public Integer getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(Integer cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }
}
