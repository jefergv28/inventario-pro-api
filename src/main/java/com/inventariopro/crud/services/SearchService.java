package com.inventariopro.crud.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inventariopro.crud.dto.CategoriaDTO;
import com.inventariopro.crud.dto.FuncionDTO;
import com.inventariopro.crud.dto.ProductoDTO;
import com.inventariopro.crud.dto.ProveedorDTO;
import com.inventariopro.crud.dto.SearchResultsDTO;
import com.inventariopro.crud.dto.UsuarioDTO;
import com.inventariopro.crud.repositories.CategoriaRepository;
import com.inventariopro.crud.repositories.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public SearchResultsDTO searchGlobal(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new SearchResultsDTO(
              Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
            );
        }

        List<ProductoDTO> productosDTO = productoRepository
            .findByNombreProductoContainingIgnoreCaseOrDescripcionProductoContainingIgnoreCase(query, query)
            .stream()
            .map(ProductoDTO::new) // convierte ProductoModel a ProductoDTO
            .collect(Collectors.toList());

        List<CategoriaDTO> categoriasDTO = categoriaRepository
            .findByNombreContainingIgnoreCase(query)
            .stream()
            .map(CategoriaDTO::new) // convierte CategoriaModel a CategoriaDTO
            .collect(Collectors.toList());

            List<ProveedorDTO> ProveedorDTO = Collections.emptyList(); // O buscarlos en repositorio si quieres
List<UsuarioDTO> UsuarioDTO = Collections.emptyList();


        List<FuncionDTO> funciones = getMatchingAppFunctions(query);

        return new SearchResultsDTO(productosDTO, categoriasDTO, ProveedorDTO, UsuarioDTO, funciones);

    }

    private List<FuncionDTO> getMatchingAppFunctions(String query) {
        List<FuncionDTO> allFunctions = List.of(
            new FuncionDTO("Crear nuevo producto", "/productos/crear"),
            new FuncionDTO("Ver lista de categorías", "/categorias"),
            new FuncionDTO("Ver stock bajo", "/dashboard")
        );

        String lowerQuery = query.toLowerCase();
        return allFunctions.stream()
            .filter(func -> func.getNombre().toLowerCase().contains(lowerQuery))
            .collect(Collectors.toList());
    }
}
