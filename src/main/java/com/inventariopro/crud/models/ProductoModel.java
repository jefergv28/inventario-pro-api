package com.inventariopro.crud.models;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importa esto

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; // Importa esto
import jakarta.persistence.ManyToOne; // Importa esto
import jakarta.persistence.Table; // Importa esto
import jakarta.persistence.Temporal; // Importa esto
import jakarta.persistence.TemporalType; // Importa esto

@Entity
@Table(name = "productos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(AuditingEntityListener.class) // <-- Agrega esta anotación
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private Double precioProducto;

    @Column(nullable = false)
    private Integer cantidadProducto;

    @Column(name = "descripcion_producto", nullable = false, length = 250)
    private String descripcionProducto;

    @Column(columnDefinition = "boolean default true")
    private boolean activo = true;

    // --- ¡CAMPO FALTANTE! ---
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_creacion", nullable = true)
    private Instant fechaCreacion;
    // --- ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference
    private CategoriaModel categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
     @JsonIgnore
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "proveedor_id", nullable = true)
    @JsonIgnore
    private ProveedorModel proveedor;

    @Column(name = "image_url")
    private String imageUrl;

    // Getters y setters...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public Double getPrecioProducto() { return precioProducto; }
    public void setPrecioProducto(Double precioProducto) { this.precioProducto = precioProducto; }

    public Integer getCantidadProducto() { return cantidadProducto; }
    public void setCantidadProducto(Integer cantidadProducto) { this.cantidadProducto = cantidadProducto; }

    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }

    public CategoriaModel getCategoria() { return categoria; }
    public void setCategoria(CategoriaModel categoria) { this.categoria = categoria; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public ProveedorModel getProveedor() {
        return proveedor;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setProveedor(ProveedorModel proveedor) {
        this.proveedor = proveedor;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // --- ¡GETTER Y SETTER FALTANTES! ---
    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    // --- ---
}