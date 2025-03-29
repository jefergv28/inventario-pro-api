package com.inventariopro.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.AlmacenModel;

@Repository
public interface AlmacenRepository extends JpaRepository<AlmacenModel, Long> {
}
