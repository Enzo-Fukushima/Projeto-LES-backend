package com.enzo.les.les.repository;

import com.enzo.les.les.model.entities.Troca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrocaRepository extends JpaRepository<Troca, Long> {

    // Buscar trocas por cliente
    List<Troca> findByClienteId(Long clienteId);

    // Buscar trocas por pedido
    List<Troca> findByPedidoId(Long pedidoId);

    // Buscar trocas por status
    List<Troca> findByStatus(String status);

    // Buscar todas as trocas pendentes (EM_TROCA)
    @Query("SELECT t FROM Troca t WHERE t.status = 'EM_TROCA' ORDER BY t.dataSolicitacao DESC")
    List<Troca> findTrocasPendentes();

    // Buscar trocas autorizadas
    @Query("SELECT t FROM Troca t WHERE t.status = 'TROCA_AUTORIZADA' ORDER BY t.dataAutorizacao DESC")
    List<Troca> findTrocasAutorizadas();

    // Verificar se já existe troca para um pedido
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Troca t WHERE t.pedido.id = :pedidoId")
    boolean existsByPedidoId(@Param("pedidoId") Long pedidoId);
}