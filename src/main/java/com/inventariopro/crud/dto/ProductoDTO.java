package com.inventariopro.crud.dto;

import com.inventariopro.crud.models.ProductoModel;

public class ProductoDTO {
    private Long id;
    private String nombreProducto;
    private Double precioProducto;
    private Integer cantidadProducto;
    private String descripcionProducto;
    private String imageUrl;
    private String categoriaNombre;
    private UsuarioDTO usuario;
    private ProveedorDTO proveedor;

    // Constructor desde la entidad
    public ProductoDTO(ProductoModel producto) {
        this.id = producto.getId();
        this.nombreProducto = producto.getNombreProducto();
        this.precioProducto = producto.getPrecioProducto();
        this.cantidadProducto = producto.getCantidadProducto();
        this.descripcionProducto = producto.getDescripcionProducto();

        if (producto.getImageUrl() != null && !producto.getImageUrl().isEmpty()) {
            this.imageUrl = "http://localhost:8000/uploads/" + producto.getImageUrl();
        } else {
            this.imageUrl = null;
        }

        this.categoriaNombre = producto.getCategoria().getNombre();
        this.usuario = new UsuarioDTO(producto.getUsuario());
        this.proveedor = producto.getProveedor() != null ? new ProveedorDTO(producto.getProveedor()) : null;
    }

    // Getters y Setters
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

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public ProveedorDTO getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorDTO proveedor) {
        this.proveedor = proveedor;
    }
}
