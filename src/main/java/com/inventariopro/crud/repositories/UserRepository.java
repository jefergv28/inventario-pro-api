package com.inventariopro.crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.User;
import com.inventariopro.crud.models.UserStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {  // Cambiado a Long
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByPasswordResetToken(String token);

      List<User> findByCreatorId(Long creatorId);
     List<User> findByStatus(UserStatus status);



}
