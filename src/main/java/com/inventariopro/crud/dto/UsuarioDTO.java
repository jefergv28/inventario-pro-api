package com.inventariopro.crud.dto;

import com.inventariopro.crud.models.User;

public class UsuarioDTO {
    private Long id;
    private String name;
    private String email;
    private String profilePicture;
     private String status;
    private String permissionsJson;

    public UsuarioDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.status = user.getStatus().name();
        this.permissionsJson = user.getPermissionsJson();
        if (user.getProfilePicture() == null || user.getProfilePicture().isEmpty()) {
            this.profilePicture = "/uploads/profile-image.jpg";
        } else {
            this.profilePicture = user.getProfilePicture();
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

     public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

     public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPermissionsJson() {
        return permissionsJson;
    }

    public void setPermissionsJson(String permissionsJson) {
        this.permissionsJson = permissionsJson;
    }
}
