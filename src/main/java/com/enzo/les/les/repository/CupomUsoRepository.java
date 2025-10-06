package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.CupomUso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomUsoRepository extends JpaRepository<CupomUso, Long> {
    boolean existsByCupomIdAndClienteId(Long cupomId, Long clienteId);
}
