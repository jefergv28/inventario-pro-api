package com.inventariopro.crud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.inventariopro.crud.dto.StockDTO;
import com.inventariopro.crud.models.StockModel;
import com.inventariopro.crud.models.User;
import com.inventariopro.crud.services.StockService;
import com.inventariopro.crud.services.UsuarioService;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar stocks del usuario autenticado
    @GetMapping
    public List<StockDTO> obtenerStocksPorUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        return stockService.obtenerStocksDTOPorUsuario(userDetails.getUsername());
    }

    // Guardar o actualizar stock
    @PostMapping
    public StockModel guardarStock(@RequestBody StockModel stock, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = usuarioService.getByEmail(userDetails.getUsername());

        if (userOptional.isPresent()) {
            stock.setUsuario(userOptional.get());
            return stockService.guardarStock(stock);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Obtener stock por ID
    @GetMapping("/{id}")
    public Optional<StockDTO> obtenerStockPorId(@PathVariable Long id) {
        return stockService.obtenerPorId(id).map(StockDTO::new);
    }

    // Eliminar stock por ID
    @DeleteMapping("/{id}")
    public String eliminarStock(@PathVariable Long id) {
        boolean eliminado = stockService.eliminarStock(id);
        return eliminado ? "Stock eliminado" : "Error al eliminar el stock";
    }
}
