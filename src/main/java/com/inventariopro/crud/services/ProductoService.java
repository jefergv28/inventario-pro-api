package com.inventariopro.crud.services;

import java.util.List;
import java.util.Optional;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.models.User;
import java.util.Map;

public interface ProductoService {

    List<ProductoModel> getProductosByUsuario(String email);

    ProductoModel saveProducto(ProductoModel producto, Long usuarioId) throws Exception;

    ProductoModel guardarProducto(ProductoModel producto);

    Optional<ProductoModel> getById(Long id);

    ProductoModel getProductoByIdAndUsuario(Long id, String email);

    Optional<ProductoModel> obtenerPorIdYUsuario(Long id, User usuario);

    ProductoModel updateById(ProductoModel datos, Long id, Long usuarioId) throws Exception;

    void eliminarProducto(Long productoId, Long usuarioId);

    List<ProductoModel> obtenerProductosActivosPorUsuario(User usuario);

    // Este método debería estar en la interfaz
    public List<ProductoModel> getProductosRecientesByUsuario(String username);

    // Y este también (si lo necesitas)
    List<Map<String, Object>> getMostMovedProductsByUsuario(String username);

}