package com.inventariopro.crud.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByname(String name);

    Optional<User> findByEmail(String email);

    public void deleteById(Long id);

    public Optional<User> findById(Long id);
}
