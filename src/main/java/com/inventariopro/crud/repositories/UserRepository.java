package com.inventariopro.crud.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventariopro.crud.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {  // Cambiado a Integer
    Optional<User> findByEmail(String email);

    // Elimina los demás métodos redundantes
}