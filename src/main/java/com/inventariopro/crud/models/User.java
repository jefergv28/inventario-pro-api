package com.inventariopro.crud.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    @Column(nullable = false)  // CONTRASEÑA sin @Lob
    private String password;

    @Builder.Default
    @Column(name = "language")
    private String language = "es";

    @Builder.Default
    @Column(name = "notifications")
    private Boolean notifications = true;

    @Lob
@Builder.Default
@Column(name = "profile_picture", columnDefinition = "TEXT DEFAULT ''")
private String profilePicture = "";

    private String passwordResetToken;
    private LocalDateTime passwordResetTokenExpiry;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private Role role;

   @Lob
@Builder.Default
@Column(name = "permissions_json", columnDefinition = "TEXT DEFAULT '{}'")
private String permissionsJson = "{}";

    @OneToMany(mappedBy = "usuario", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<ProductoModel> productos;

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    // equals y hashCode basados solo en id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // Getters y Setters adicionales
    public List<ProductoModel> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoModel> productos) {
        this.productos = productos;
    }

    public String getPermissionsJson() {
        return permissionsJson;
    }

    public void setPermissionsJson(String permissionsJson) {
        this.permissionsJson = permissionsJson;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
