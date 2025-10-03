package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.SaldoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaldoEstoqueRepository extends JpaRepository<SaldoEstoque, Long> {

}
