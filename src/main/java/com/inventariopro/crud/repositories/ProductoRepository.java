package com.inventariopro.crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;

// Indica que esta interfaz es un componente de repositorio de Spring
@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {
  List<ProductoModel> findByUsuario(User usuario);

  Optional<ProductoModel> findByIdAndUsuario(Long id, User usuario);

  List<ProductoModel> findByUsuarioAndActivoTrue(User usuario);

  // --- ¡MÉTODO FALTANTE AÑADIDO! ---
  // Este método es crucial para buscar un producto por su ID y el ID del usuario propietario.
  Optional<ProductoModel> findByIdAndUsuarioId(Long id, Long usuarioId);

  // Cuenta el total de productos para un usuario
  long countByUsuarioId(Long usuarioId);

  // Corregido: Ahora busca la propiedad 'cantidadProducto' en el modelo
  long countByUsuarioIdAndCantidadProductoLessThan(Long usuarioId, int cantidadProducto);


   // Nuevo método: Obtiene los productos más recientes de un usuario, ordenados por fecha de creación descendente y limitados a, por ejemplo, 5.
    List<ProductoModel> findByUsuarioIdAndActivoTrueOrderByFechaCreacionDesc(Long usuarioId);

    long countByCantidadProductoLessThan(int cantidad);

    // Este método buscará la cadena de texto en nombreProducto O en descripcionProducto
List<ProductoModel> findByNombreProductoContainingIgnoreCaseOrDescripcionProductoContainingIgnoreCase(String nombre, String descripcion);
}
