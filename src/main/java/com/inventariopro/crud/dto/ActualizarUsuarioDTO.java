package com.inventariopro.crud.dto;

public class ActualizarUsuarioDTO {
    private String name;
    private String email;
    private String language;
    private boolean notifications;
    private String password; // opcional

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public boolean isNotifications() { return notifications; }
    public void setNotifications(boolean notifications) { this.notifications = notifications; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
