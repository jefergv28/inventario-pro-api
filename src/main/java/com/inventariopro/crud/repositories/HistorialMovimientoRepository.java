package com.inventariopro.crud.repositories;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.dto.ProductoMovidoDTO;
import com.inventariopro.crud.models.HistorialMovimientoModel;

@Repository
public interface HistorialMovimientoRepository extends JpaRepository<HistorialMovimientoModel, Long> {

    @Transactional
    void deleteByProductoId(Long productoId);

    // Método corregido: Ahora carga ansiosamente (JOIN FETCH) el usuario y el producto
    @Query("SELECT h FROM HistorialMovimientoModel h JOIN FETCH h.usuario u JOIN FETCH h.producto p WHERE u.email = :email")
    List<HistorialMovimientoModel> findByUsuarioEmailWithDetails(@Param("email") String email);

    List<HistorialMovimientoModel> findByProductoId(Long productoId);


   @Query("SELECT new com.inventariopro.crud.dto.ProductoMovidoDTO(hm.producto.nombreProducto, COUNT(hm)) " +
       "FROM HistorialMovimientoModel hm " + // <-- CAMBIO AQUÍ: HistorialMovimientoModel
       "WHERE hm.producto.usuario.id = :usuarioId " +
       "GROUP BY hm.producto.nombreProducto " +
       "ORDER BY COUNT(hm) DESC")
List<ProductoMovidoDTO> findProductosMasMovidos(@Param("usuarioId") Long usuarioId);


// Cuenta el total de movimientos para un usuario
    long countByUsuarioId(Long usuarioId);
}