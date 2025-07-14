package com.inventariopro.crud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventariopro.crud.models.StockModel;

@Repository
public interface StockRepository extends JpaRepository<StockModel, Long> {

List<StockModel> findByUsuario_Email(String email);

}
