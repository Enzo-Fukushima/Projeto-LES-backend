package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Aqui podemos criar filtros customizados futuramente (ex: buscar por nome/email)
}
