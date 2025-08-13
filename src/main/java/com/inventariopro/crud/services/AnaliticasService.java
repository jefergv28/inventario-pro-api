package com.inventariopro.crud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventariopro.crud.dto.ProductoMovidoDTO;
import com.inventariopro.crud.repositories.CategoriaRepository;
import com.inventariopro.crud.repositories.HistorialMovimientoRepository;
import com.inventariopro.crud.repositories.ProductoRepository;

@Service
public class AnaliticasService {

  @Autowired
  private HistorialMovimientoRepository historialMovimientoRepository;

    @Autowired
    private ProductoRepository productoRepository; // Repositorio para manejar los productos

    @Autowired
    private CategoriaRepository categoriaRepository; // Repositorio para manejar las categorías

  public List<ProductoMovidoDTO> getProductosMasMovidos(Long usuarioId) {
    return historialMovimientoRepository.findProductosMasMovidos(usuarioId);
  }

    public long getTotalProductos(Long usuarioId) {
        // Método en el repositorio para contar productos por usuario
        return productoRepository.countByUsuarioId(usuarioId);
    }

    public long getProductosConStockBajo(Long usuarioId) {
        int umbralStock = 10;
        // Cambia 'Cantidad' por 'CantidadProducto'
        return productoRepository.countByUsuarioIdAndCantidadProductoLessThan(usuarioId, umbralStock);
    }

    public long getUltimosMovimientos(Long usuarioId) {
        // Aquí podrías contar todos los movimientos recientes de los últimos 30 días, por ejemplo
        // Por ahora, solo devolveremos el conteo total para simplificar
        return historialMovimientoRepository.countByUsuarioId(usuarioId);
    }

    public long getTotalCategorias(Long usuarioId) {
        // Método en el repositorio para contar las categorías por usuario
        return categoriaRepository.countDistinctByUsuarioId(usuarioId);
    }




}