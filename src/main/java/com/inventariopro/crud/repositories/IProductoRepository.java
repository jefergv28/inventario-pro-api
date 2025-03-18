package com.inventariopro.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ProductoModel;

// Indica que esta interfaz es un componente de repositorio de Spring
@Repository
public interface IProductoRepository extends JpaRepository<ProductoModel, Long> {
    // Hereda de JpaRepository para realizar operaciones CRUD en la entidad ProductoModel
    // No es necesario escribir métodos adicionales, ya que JpaRepository proporciona métodos como:
    // - findAll() → Obtener todos los productos
    // - findById(id) → Buscar un producto por su ID
    // - save(producto) → Guardar o actualizar un producto
    // - deleteById(id) → Eliminar un producto por su ID
}
