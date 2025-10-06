package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.SaldoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface SaldoEstoqueRepository extends JpaRepository<SaldoEstoque, Long> {

    // Busca com lock pessimista para evitar oversell
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SaldoEstoque s join fetch s.livro l where l.id = :livroId")
    Optional<SaldoEstoque> findByLivroIdForUpdate(@Param("livroId") Long livroId);
}
