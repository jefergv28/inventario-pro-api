package com.inventariopro.crud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventariopro.crud.dto.ProductoMovidoDTO;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.services.AnaliticasService;
import com.inventariopro.crud.services.UsuarioService;


@RestController
@RequestMapping("/api/analiticas") // Endpoint base para las analíticas
public class AnaliticasController {

  @Autowired
  private AnaliticasService analiticasService;

  @Autowired
  private UsuarioService userService; // Lo usamos para obtener el usuario autenticado

    private User getUserFromUserDetails(UserDetails userDetails) {
        return userService.obtenerUsuarioPorEmail(userDetails.getUsername());
    }

  @GetMapping("/productos-mas-movidos")
  public ResponseEntity<List<ProductoMovidoDTO>> getProductosMasMovidos(@AuthenticationPrincipal UserDetails userDetails) {
    User user = getUserFromUserDetails(userDetails);
    List<ProductoMovidoDTO> productosMasMovidos = analiticasService.getProductosMasMovidos(user.getId());
    return ResponseEntity.ok(productosMasMovidos);
  }

    @GetMapping("/total-productos")
    public ResponseEntity<Long> getTotalProductos(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        long totalProductos = analiticasService.getTotalProductos(user.getId());
        return ResponseEntity.ok(totalProductos);
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<Long> getProductosConStockBajo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        long productosConStockBajo = analiticasService.getProductosConStockBajo(user.getId());
        return ResponseEntity.ok(productosConStockBajo);
    }

    @GetMapping("/ultimos-movimientos")
    public ResponseEntity<Long> getUltimosMovimientos(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        long ultimosMovimientos = analiticasService.getUltimosMovimientos(user.getId());
        return ResponseEntity.ok(ultimosMovimientos);
    }

    @GetMapping("/total-categorias")
    public ResponseEntity<Long> getTotalCategorias(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        long totalCategorias = analiticasService.getTotalCategorias(user.getId());
        return ResponseEntity.ok(totalCategorias);
    }
}