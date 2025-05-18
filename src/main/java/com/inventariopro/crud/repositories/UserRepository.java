package com.inventariopro.crud.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {  // Cambiado a Long
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
}
