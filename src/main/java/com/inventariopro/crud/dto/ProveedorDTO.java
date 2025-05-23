package com.inventariopro.crud.dto;

import com.inventariopro.crud.models.ProveedorModel;

public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String contacto;
    private String direccion; // <-- Agregado
    private UsuarioDTO usuario;

    public ProveedorDTO(ProveedorModel proveedor) {
        this.id = proveedor.getId();
        this.nombre = proveedor.getNombre();
        this.contacto = proveedor.getContacto();
        this.direccion = proveedor.getDireccion(); // <-- Agregado
        this.usuario = new UsuarioDTO(proveedor.getUsuario());
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getNombre() {
      return nombre;
    }

    public void setNombre(String nombre) {
      this.nombre = nombre;
    }

    public String getContacto() {
      return contacto;
    }

    public void setContacto(String contacto) {
      this.contacto = contacto;
    }

    public String getDireccion() {  // <-- Agregado
      return direccion;
    }

    public void setDireccion(String direccion) {  // <-- Agregado
      this.direccion = direccion;
    }

    public UsuarioDTO getUsuario() {
      return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
      this.usuario = usuario;
    }
}
