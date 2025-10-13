package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Os métodos básicos (findAll, findById, save, delete, etc.) 
    // são herdados do JpaRepository automaticamente
}